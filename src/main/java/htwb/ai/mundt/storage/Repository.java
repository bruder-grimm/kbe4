package htwb.ai.mundt.storage;

import org.brudergrimm.jmonad.option.Option;
import org.brudergrimm.jmonad.tried.Try;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.logging.Logger;

/** @param <Value>> The Type of the DAO
 *  @param <Key> The Type of the PrimaryKey */
public abstract class Repository<Key, Value extends Identifiable<Key>> implements IRepository<Key, Value> {
    protected static final Logger logger = Logger.getLogger(Repository.class.getName());

    protected final EntityManagerFactory emf;
    protected final Class<Value> type;

    public Repository(EntityManagerFactory emf, Class<Value> type) {
        this.type = type;
        this.emf = emf;
    }

    /** Return all of this entity
     * @return all of this entity */
    public List<Value> getAll() {
        return withSessionDo(session -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();

            CriteriaQuery<Value> cq = cb.createQuery(type);
            CriteriaQuery<Value> all = cq.select(cq.from(type));

            return session.createQuery(all).getResultList();
        }).getOrElse(List.of());
    }

    /** Find that ONE entity that you need or something
     *  @param id the primary key
     *  @return the found entity */
    public Option<Value> findBy(Key id) {
        return withSessionDo(session ->
                initializeAndUnproxy(session.getReference(type, id))
        ).toOption();
    }

    /** Persist the entity to the database (change state to managed state)
     *  I'm working overtime trying to work around this HORRIBLE STATE PATTERN
     *  Can you tell I'm tired of java?
     *  @param entity the instance to make managed
     *  @return the id of the now managed instance. The instance will have changed states. Just discard it */
    public Try<Key> create(Value entity) {
        return withTransactionSessionDo(session -> {
            session.persist(entity);
            return entity.getId();
        }).onFailure(ex ->
                logger.info(String.format("Couldn't create entity. Reason: %s", ex.getMessage()))
        );
    }

    /** Merges the given entity with its managed counterpart because we've been doing stuff hibernate shouldn't know about
     *  pssssst
     *  @param entity the entity
     *  @return if the merge was successful */
    public boolean update(Value entity) {
        return withTransactionSessionDo(session ->
                Option.apply(session.find(type, entity.getId()))
                        .map(ignored -> {
                            session.merge(entity);
                            return true;
                        }).getOrElse(false)
        ).onFailure(ex ->
                logger.info(String.format("Couldn't update entity with id '%s', reason: %s", entity.getId(), ex.getMessage()))
        ).getOrElse(false);
    }

    /** detach the entity (also known as deleting it to normal people)
     * @param id pk of the record to get rid of
     * @return if the endeavour was triumphant */
    public boolean delete(Key id) {
        return withTransactionSessionDo(session ->
                Try.apply(() -> initializeAndUnproxy(session.find(type, id)))
                        .map(entity -> {
                            session.remove(entity);
                            return true;
                        }).getOrElse(false)
        ).onFailure(ex ->
                logger.info(String.format("Couldn't delete entity with id '%s', reason: %s", id, ex.getMessage()))
        ).getOrElse(false);
    }

    /** make this usable with try with resources
     * @throws IOException if idk something happened */
    public void close() throws IOException {
        try {
            emf.close();
        } catch (IllegalStateException e) {
            throw new IOException(e);
        }
    }

    /** Wraps whatever you give it in a transation, will roll back on failure
     *  This will never, ever, ever(!), throw
     *  @param query what you want to execute
     *  @param <A> the return type of your query
     *  @return A */
    private <A> Try<A> withTransactionSessionDo(Function<EntityManager, A> query) {
        int connectionId = ThreadLocalRandom.current().nextInt(1, 10);
        logger.info(String.format("Opening connection (%s) in transaction...", connectionId));

        return Try.apply(emf::createEntityManager)
                .map(session -> {
                    try {
                        return applyInTransaction(session, query);
                    } catch (IllegalStateException ex) {
                        logger.info(String.format("Rolling back transaction. Reason: %s", ex.getMessage()));
                        throw rollbackAndCreateDeescelatedException(session, ex);
                    } finally {
                        session.close();
                        logger.info(String.format("Transaction commited and connection (%s) closed", connectionId));
                    }
                });
    }

    private <A> Try<A> withSessionDo(Function<EntityManager, A> query) {
        int connectionId = ThreadLocalRandom.current().nextInt(1, 10);
        logger.info(String.format("Opening connection (%s)...", connectionId));

        return Try.apply(emf::createEntityManager)
                .flatMap(session -> {

                    Try<A> result = Try.apply(() -> query.apply(session));
                    session.close();

                    logger.info(String.format("Connection (%s) closed", connectionId));
                    return result;
                });
    }

    /** This Function is not pure, the runnable can contain an object that might have been mutated.
     *  Use with caution, not threadsafe, not predictable, not testable, nothing. I hate java
     *  @param em the EntityManager
     *  @param action The thing you want to have managed or detached or whatever really I don't even care anymore */
    private static <A> A applyInTransaction(EntityManager em, Function<EntityManager, A> action) throws IllegalStateException {
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        A result = action.apply(em);
        transaction.commit();

        return result;
    }

    /** Hallo Steven
     *  Du warst ja dabei als es nicht geklappt hat, falls es dich interessiert also hier:
     *
     *  Der Song war ein HibernateProxy, der Typ von Song war dann also nicht Song sondern Song_$$_{hibernate_session_id}
     *  Die Idee ist dass der Song im letzten moment deproxied wird, dafür wird aber die session gebraucht, die, wenn
     *  wir im controller unseren SongProxy zurückgeben, lange geschlossen und wieder dem pool zurückgegeben wurde.
     *
     *  WriterInterceptorExecutor ruft in seiner Collection von MessageBodyWriters bei allen isWriteable(Type type, ...)
     *  auf - Nach der TypeErasure ist List<Song> nur noch List und kann deshalb geschrieben werden weil es auch ohne
     *  Klassen als Entitys zu registrieren Writer dafür gibt.
     *  Für den Typ Song_$$_{hibernate_session_id} gibt es natürlich keinen MessageBodyWriter.
     *
     *  Indem wir die Instanz hier mit getImplementation zwingen zu deproxien währen die session noch nicht geschlossen
     *  wurde, können wir erstens Fehler sofort werfen lassen (die dann von Try.apply() aufgefangen werden) und
     *  Instanzen sofort auflösen
     *
     *  @param entity The entity to unproxy
     *  @param <T> the type of the entity
     *  @return unproxied entity */
    public static <T> T initializeAndUnproxy(final T entity) {
        Hibernate.initialize(entity);

        if (entity instanceof HibernateProxy) {
            logger.info(String.format("Unproxieing instance of type '%s'", entity.getClass().getName()));
            @SuppressWarnings("unchecked")
            T resolved = (T) ((HibernateProxy) entity)
                    .getHibernateLazyInitializer()
                    .getImplementation();
            logger.info(String.format("Got entity of class '%s'", resolved.getClass().getName()));

            return resolved;
        }
        return entity;
    }

    /** Deescelate the IllegalStateException and rolling back the transaction like it aint anybodys business
     *  @param em the session
     *  @param databaseError the error to deescalate
     *  @return the deescalated error I'm returning exceptions what up java cheesebags */
    private static PersistenceException rollbackAndCreateDeescelatedException(EntityManager em, Throwable databaseError) {
        em.getTransaction().rollback();
        em.close();
        return new PersistenceException(databaseError.getMessage());
    }
}


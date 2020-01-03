package htwb.ai.mundt.storage;

import org.brudergrimm.jmonad.option.Option;

import javax.inject.Inject;
import java.util.logging.Logger;

public abstract class CRUD<Key, Value extends Identifiable<Key>> implements ICRUD<Key, Value> {
    protected static final Logger logger = Logger.getLogger(CRUD.class.getName());
    protected final IRepository<Key, Value> repository;

    @Inject
    protected CRUD(IRepository<Key, Value> repository) {
        this.repository = repository;
    }

    public Option<Key> create(Value value) {
        logger.info(String.format("Persisting %s to database", value.toString()));
        return repository.create(value).toOption();
    }

    public Option<Value> read(Key id) {
        logger.info(String.format("Retrieving entity with id '%s'", id));
        return repository.findBy(id);
    }

    public boolean update(Value value) {
        logger.info(String.format("Updating %s in database", value.toString()));
        return repository.update(value);
    }

    public boolean delete(Key id) {
        logger.info(String.format("Deleting entity with id '%s'", id));
        return repository.delete(id);
    }
}

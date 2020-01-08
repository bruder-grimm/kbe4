package htwb.ai.mundt.api.database;

import htwb.ai.mundt.user.IUserRepository;
import htwb.ai.mundt.user.User;
import org.brudergrimm.jmonad.option.Option;
import org.brudergrimm.jmonad.tried.Success;
import org.brudergrimm.jmonad.tried.Try;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements IUserRepository {
    private static Map<String, User> storage;

    public InMemoryUserRepository() {
        storage = new ConcurrentHashMap<>();
        initSomeContacts();
    }
    private static void initSomeContacts() {
        //(userId,      key,            firstname, lastName)
        //('mmuster', 'ENCRYPTEDKEY1', 'Maxime', 'Muster');
        //('eschuler', 'ENCRYPTEDKEY2', 'Elena', 'Schuler');

        User user1 = new User();
        user1.setUserid("mmuster");
        user1.setKey("ENCRYPTEDKEY1");
        user1.setFirstname("Maxime");
        user1.setLastname("Muster");
        storage.put(user1.getId(), user1);

        User user2 = new User();
        user2.setUserid("eschuler");
        user2.setKey("ENCRYPTEDKEY2");
        user2.setFirstname("Elena");
        user2.setLastname("Schuler");
        storage.put(user2.getId(), user2);

    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Option<User> findBy(String id) {
        return Option.apply(storage.get(id));
    }

    @Override
    public Try<String> create(User entity) {
        storage.put(entity.getId(), entity);
        return Success.apply(entity.getId());
    }

    @Override
    public boolean update(User entity) {
        if(storage.containsKey(entity.getId())){
            storage.put(entity.getId(),entity);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        if(storage.containsKey(id)){
            storage.remove(id);
            return true;
        };
        return false;
    }

    @Override
    public void close() { }
}

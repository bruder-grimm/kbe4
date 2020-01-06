package htwb.ai.mundt.user;

import htwb.ai.mundt.storage.CRUD;
import htwb.ai.mundt.storage.Repository;
import org.brudergrimm.jmonad.option.Option;

import javax.inject.Inject;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class UserService extends CRUD<String, User> implements IUserService {

    // we need to achieve bidirectional mapping because iterating over a map KILLS performance
    private final Map<String, User> authorizedKeyToUser = new ConcurrentHashMap<>();
    private final Map<User, String> authorizedUserToKey = new ConcurrentHashMap<>();

    private final Supplier<String> keygen;

    @Inject
    public UserService(IUserRepository repository) {
        super(repository);

        this.keygen = () -> {
            byte[] bytes = new byte[64];
            ThreadLocalRandom.current().nextBytes(bytes);

            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(bytes);
        };
    }

    protected UserService(Repository<String, User> repository, Supplier<String> keygen) {
        super(repository);
        this.keygen = keygen;
    }

    public Option<User> getUserWith(String id, String key) {
        return repository.findBy(id)
                .filter(user -> user.getKey().equals(key));
    }

    public String getAuthorizationKeyFor(User user) {
        String token;
        if (!authorizedUserToKey.containsKey(user)) {
            token = keygen.get();
            addToIssedKeys(user, token);
        } else {
            token = authorizedUserToKey.get(user);
        }

        return token;
    }

    public boolean isAuthorized(String token) {
        return authorizedKeyToUser.containsKey(token);
    }

    public Option<User> getUserFor(String token) {
        return Option.apply(authorizedKeyToUser.get(token));
    }

    private void addToIssedKeys(User user, String key) {
        this.authorizedUserToKey.put(user, key);
        this.authorizedKeyToUser.put(key, user);
    }
}

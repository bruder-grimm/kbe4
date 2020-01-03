package htwb.ai.mundt.user;

import htwb.ai.mundt.storage.CRUD;
import htwb.ai.mundt.storage.Repository;
import org.brudergrimm.jmonad.option.Option;

import javax.inject.Inject;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class UserService extends CRUD<String, User> implements IUserService {
    private final Map<Integer, String> issuedKeys = new ConcurrentHashMap<>();
    private final Function<Integer, String> keygen;

    @Inject
    public UserService(IUserRepository repository) {
        super(repository);

        this.keygen = (Integer ignored) -> {
            byte[] bytes = new byte[64];
            ThreadLocalRandom.current().nextBytes(bytes);

            return Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(bytes);
        };
    }

    protected UserService(Repository<String, User> repository, Function<Integer, String> keygen) {
        super(repository);
        this.keygen = keygen;
    }

    public Option<User> getUserWith(String id, String key) {
        return repository.findBy(id)
                .filter(user -> user.getKey().equals(key));
    }

    public String getAuthorizationKeyFor(User user) {
        return issuedKeys.computeIfAbsent(user.hashCode(), keygen);
    }

    public boolean isAuthorized(String token) {
        return issuedKeys.containsValue(token);
    }
}

package htwb.ai.mundt.user;

import htwb.ai.mundt.storage.ICRUD;
import org.brudergrimm.jmonad.option.Option;

public interface IUserService extends ICRUD<String, User> {
    Option<User> getUserWith(String id, String key);
    String getAuthorizationKeyFor(User user);
    boolean isAuthorized(String token);
}

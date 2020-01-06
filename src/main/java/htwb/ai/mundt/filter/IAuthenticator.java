package htwb.ai.mundt.filter;

import htwb.ai.mundt.user.User;
import org.brudergrimm.jmonad.option.Option;

public interface IAuthenticator {
    /** authenticates a token
     *  @param token the token
     *  @return if its valid */
    boolean authenticate(String token);

    /** Returns the user that was authenticated witht this token
     *  @param token the token
     *  @return the user */
    Option<User> getAuthenticatedUser(String token);
}

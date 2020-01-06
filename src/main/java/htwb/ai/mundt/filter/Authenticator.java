package htwb.ai.mundt.filter;

import htwb.ai.mundt.user.IUserService;
import htwb.ai.mundt.user.User;
import org.brudergrimm.jmonad.option.Option;

import javax.inject.Inject;

public class Authenticator implements IAuthenticator {

    private final IUserService userService;

    @Inject
    public Authenticator(IUserService userService) {
        this.userService = userService;
    }

    @Override public boolean authenticate(String token) {
        return userService.isAuthorized(token);
    }

    @Override public Option<User> getAuthenticatedUser(String token) {
        return userService.getUserFor(token);
    }
}
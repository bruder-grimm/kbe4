package htwb.ai.mundt.filter;

import htwb.ai.mundt.user.IUserService;

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
}
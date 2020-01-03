package htwb.ai.mundt.services;

import htwb.ai.mundt.user.IUserService;
import htwb.ai.mundt.user.UserService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

@Path("/auth")
public class UserController extends RestController {
    private final IUserService userService;

    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Produces(TEXT_PLAIN)
    public Response get(@QueryParam("userId") String userId, @QueryParam("key") String key) {
        return userService.getUserWith(userId, key)
                .map(user -> {
                    String token = userService.getAuthorizationKeyFor(user);
                    logInfo(String.format("Returning token '%s' for User: '%s' with key: '%s' ", token.substring(0, 35), userId, key));

                    return Response.ok(token).type(TEXT_PLAIN);
                })
                .orElseGet(() -> {
                        logInfo(String.format("Recieved invalid get for userId: '%s', key: '%s'. Returning 401", userId, key));
                        return Response.status(UNAUTHORIZED);
                })
        .build();

    }
}

package htwb.ai.mundt.filter;

import htwb.ai.mundt.user.User;
import org.brudergrimm.jmonad.option.Option;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

@Provider
public class AuthorizationFilter implements ContainerRequestFilter {
    private static final Logger logger = Logger.getLogger(AuthorizationFilter.class.getName());
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final IAuthenticator authenticator;

    @Inject
    public AuthorizationFilter(IAuthenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override public void filter(ContainerRequestContext requestContext) {
        Option<String> possibleToken =
                Option.apply(requestContext.getHeaderString(AUTHORIZATION_HEADER))
                        .filter(token -> !token.trim().isEmpty());

        boolean isAuthenticated = possibleToken
                .map(authenticator::authenticate)
                .getOrElse(false);

        String requestPath = requestContext.getUriInfo().getPath();

        if (possibleToken.isEmpty()) {

            List<String> parts = Arrays.asList(requestPath.split("/"));

            if (!parts.contains("auth")) {
                logger.info(String.format("No token given, denying request to %s", requestPath));
                abort(requestContext);
            }
        } else if (!isAuthenticated) {

            logger.info(String.format(
                    "Wasn't able to authenticate token '%s', denying request to %s",
                    possibleToken.get(),
                    requestPath));
            abort(requestContext);
        } else {

            Option<User> authenticatedUser = authenticator.getAuthenticatedUser(possibleToken.get());

            // relatively certain this is not going to happen but making sure never killed anyone
            if (authenticatedUser.isEmpty()) abort(requestContext);

            // we could set a SecurityContext here and access the UserPrincipal from within the controller methods
            // but since I just don't care enough setting a property will do
            requestContext.setProperty("authenticatedUser", authenticatedUser.get());
        }
    }

    private void abort(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.status(UNAUTHORIZED).build());
    }
}
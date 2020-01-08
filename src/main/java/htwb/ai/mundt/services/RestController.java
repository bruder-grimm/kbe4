package htwb.ai.mundt.services;

import htwb.ai.mundt.user.User;

import javax.ws.rs.container.ContainerRequestContext;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

public abstract class RestController {
    private static final Logger logger = Logger.getLogger(RestController.class.getName());

    static void logInfo(String info) {
        logger.log(INFO, String.format("Thread-%s: %s", Thread.currentThread().getId(), info));
    }

    /** This will throw an exception if called in an unauthorized setting
     *  @param context the context of the request
     *  @return the currently authenticated user */
    static User getUserFromContext(ContainerRequestContext context) {
        User authenticatedUser = (User) context.getProperty("authenticatedUser");
        if (authenticatedUser == null) {
            throw new RuntimeException("Entered protected route without authentication");
        }

        return authenticatedUser;
    }
}

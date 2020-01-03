package htwb.ai.mundt.services;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

public abstract class RestController {
    private static final Logger logger = Logger.getLogger(RestController.class.getName());
    static void logInfo(String info) {
        logger.log(INFO, String.format("Thread-%s: %s", Thread.currentThread().getId(), info));
    }
}

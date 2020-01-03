package htwb.ai.mundt;

import htwb.ai.mundt.config.DependencyBinder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LocalTest {
    public static HttpServer startServer() {
        ResourceConfig config = new ResourceConfig();
        config.packages("htwb.ai.mundt");
        config.register(new DependencyBinder());
        return GrizzlyHttpServerFactory.createHttpServer(URI.create("http://localhost:8080/"), config);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Starting HTTP Server");
        final HttpServer server = startServer();

        System.out.println("Setting Log Level to INFO");
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(Level.INFO);
        for (Handler h : rootLogger.getHandlers()) {
            h.setLevel(Level.INFO);
        }

        System.out.println("Hit enter to stop it...");
        System.in.read();
        server.stop();
    }
}


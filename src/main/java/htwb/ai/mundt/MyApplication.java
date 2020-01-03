package htwb.ai.mundt;

import htwb.ai.mundt.config.DependencyBinder;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/rest")
public class MyApplication extends ResourceConfig {
    public MyApplication() {
        System.out.print("Registering... ");

        register(new DependencyBinder());

        packages("htwb.ai.mundt");

        System.out.println("Done!");
    }
}


package com.revolut.dmylnev;


import com.revolut.dmylnev.database.DbConnectionProvider;
import com.revolut.dmylnev.database.DbConnectionProviderFactory;
import com.revolut.dmylnev.database.h2.H2ConnectionProvider;
import com.revolut.dmylnev.rest.jetty.JettyFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.eclipse.jetty.server.Server;
import javax.annotation.Nonnull;
import java.util.Objects;

import static org.apache.logging.log4j.core.LifeCycle.State.STARTED;

/**
 *
 */

public class App {

    private static final Logger log = LogManager.getLogger(App.class);

    private final static String portStr = "8090";

    static {

        final LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);

        Objects.requireNonNull(context);

        context.start();

        int i = 100;

        while (i-- > 0) {

            if(context.getState() != STARTED) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}
            }

        }

        if(context.getState() != STARTED) {
            System.err.println("Log4j2 hasn't been initialized after 10 seconds! Halting down system!");
            throw new IllegalStateException("Log4j2 hasn't been initialized after 10 seconds!");
        }
    }

    public static void main( String[] args ) {

        log.info("App starting");

        @Nonnull final DbConnectionProvider dbProvider = new H2ConnectionProvider("test", "user", "password");

        try {

            dbProvider.init();

            DbConnectionProviderFactory.init(dbProvider);

            log.info("DB successfully init");

            //----------------------------------------------------------------------------------------------------------

            @Nonnull final String port = System.getProperty("port", portStr);

            @Nonnull final Server server = JettyFactory.createJetty(Integer.parseInt(port));

            server.start();

            log.info("Jetty successfully started on port [{}]", port);

            server.join();

        } catch (Throwable th) {
            log.error("Start server error", th);
        } finally {
            dbProvider.shutdown();
        }
    }

}

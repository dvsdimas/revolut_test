package com.revolut.dmylnev;


import com.revolut.dmylnev.database.DbConnectionProvider;
import com.revolut.dmylnev.database.h2.H2ConnectionProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import javax.annotation.Nonnull;
import java.util.Objects;

import static org.apache.logging.log4j.core.LifeCycle.State.STARTED;

/**
 * Hello world!
 *
 */

public class App {

    private static final Logger log = LogManager.getLogger(App.class);

    private final static int port = 8090;

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

            @Nonnull final ThreadPool threadPool = new QueuedThreadPool(10, 1, 1000);

            @Nonnull final Server server = new Server(threadPool);

            @Nonnull final ServerConnector connector = new ServerConnector(server);

            connector.setPort(port);

            server.setConnectors(new Connector[] {connector});

            @Nonnull final ServletContextHandler context = new ServletContextHandler();

            context.setContextPath("/");
//            context.addServlet(HelloServlet.class, "/hello");
//            context.addServlet(AsyncEchoServlet.class, "/echo/*");

            @Nonnull final HandlerCollection handlers = new HandlerCollection();

            handlers.setHandlers(new Handler[]{context, new DefaultHandler()});

            server.setHandler(handlers);

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

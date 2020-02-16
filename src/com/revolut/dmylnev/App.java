package com.revolut.dmylnev;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import java.util.Objects;

import static org.apache.logging.log4j.core.LifeCycle.State.STARTED;

/**
 * Hello world!
 *
 */

public class App {

    private static final Logger log = LogManager.getLogger(App.class);

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



    }

}

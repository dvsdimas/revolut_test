package com.revolut.dmylnev.test.base;

import com.revolut.dmylnev.database.DbConnectionProvider;
import com.revolut.dmylnev.database.h2.H2ConnectionProvider;
import com.revolut.dmylnev.rest.jetty.JettyFactory;
import com.revolut.dmylnev.services.ServicesProvider;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import javax.annotation.Nonnull;

/**
 * @author dmylnev
 * @since 17.02.2020
 */

public abstract class BaseDBTest {

    private static @Nonnull final DbConnectionProvider dbProvider = new H2ConnectionProvider("test", "user", "password");

    private static volatile Server server;

    @BeforeClass
    public static void init() throws Exception {

        dbProvider.init();

        ServicesProvider.init(dbProvider);

        server = JettyFactory.createJetty(8080);

        server.start();
    }

    @AfterClass
    public static void shutdown() throws Exception {

        server.stop();

        server.join();

        dbProvider.shutdown();
    }

}

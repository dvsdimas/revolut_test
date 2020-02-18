package com.revolut.dmylnev.test;

import com.revolut.dmylnev.rest.jetty.JettyFactory;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * @author dmylnev
 * @since 18.02.2020
 */

public class BaseRestTest extends BaseDBTest {

    private static volatile Server server;

    @BeforeClass
    public static void init() throws Exception {

        server = JettyFactory.createJetty(8080);

        server.start();
    }

    @AfterClass
    public static void shutdown() throws Exception {

        server.stop();

        server.join();
    }

}

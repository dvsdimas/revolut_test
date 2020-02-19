package com.revolut.dmylnev.test.base;

import com.revolut.dmylnev.entity.Account;
import com.revolut.dmylnev.rest.jetty.JettyFactory;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import javax.annotation.Nonnull;

/**
 * @author dmylnev
 * @since 18.02.2020
 */

public class BaseRestTest extends BaseDBTest {

    private static volatile Server server;

    protected static final int port = 8080;

    private static final String host = "http://localhost:";
    private static final String base = host + port;
    private static final String account = base + "/account";

    @BeforeClass
    public static void init() throws Exception {

        server = JettyFactory.createJetty(port);

        server.start();
    }

    @AfterClass
    public static void shutdown() throws Exception {

        server.stop();

        server.join();
    }

//    public @Nonnull Account restCreateAccount() {
//
//    }

}

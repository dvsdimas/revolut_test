package com.revolut.dmylnev.test.base;

import com.revolut.dmylnev.entity.Account;
import com.revolut.dmylnev.rest.jetty.JettyFactory;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.util.FormContentProvider;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.Fields;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

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

    public static @Nonnull Account restCreateAccount(@Nonnull final String currency, @Nonnull final UUID uuid) throws Exception {

        Objects.requireNonNull(currency);
        Objects.requireNonNull(uuid);

        @Nonnull final HttpClient httpClient = new HttpClient();

        try {

            httpClient.start();

            @Nonnull final Fields fields = new Fields();

            fields.put("currency", currency);
            fields.put("uuid", uuid.toString());

            final String response = httpClient.POST(account).content(new FormContentProvider(fields)).send().getContentAsString();

            Assert.assertNotNull(response);

            // todo

//            return new Account();

            return null;

        } finally {
            httpClient.stop();
        }

    }

    public static @Nullable Account restGetAccount(final long id) throws Exception {

        @Nonnull final HttpClient httpClient = new HttpClient();

        try {

            httpClient.start();

            final String response = httpClient.GET(account + "/" + id).getContentAsString();

            Assert.assertNotNull(response);

            // todo

            return null;

        } finally {
            httpClient.stop();
        }

    }

}

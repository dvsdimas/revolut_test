package com.revolut.dmylnev.test.base;

import com.revolut.dmylnev.entity.Account;
import com.revolut.dmylnev.rest.jetty.JettyFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.FormContentProvider;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.Fields;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.UUID;

/**
 * @author dmylnev
 * @since 18.02.2020
 */

public class BaseRestTest extends BaseDBTest {

    private static final Logger log = LogManager.getLogger(BaseRestTest.class);

    private static volatile Server server;

    protected static final int port = 8080;

    private static final String host = "http://localhost:";
    private static final String base = host + port;
    private static final String account = base + "/account";

    @BeforeClass
    public static void init() throws Exception {

        BaseDBTest.init();

        server = JettyFactory.createJetty(port);

        server.start();
    }

    @AfterClass
    public static void shutdown() throws Exception {

        server.stop();

        server.join();

        BaseDBTest.shutdown();
    }

    public static @Nonnull Account restCreateAccount(@Nonnull final String currency, @Nonnull final UUID uuid) throws Exception {

        Objects.requireNonNull(currency);
        Objects.requireNonNull(uuid);

        @Nonnull final HttpClient httpClient = new HttpClient();

        try {

            httpClient.start();

            @Nonnull final Fields fields = new Fields();

            fields.put(Account.PARAM_CURRENCY, currency);
            fields.put(Account.PARAM_UUID, uuid.toString());

            final ContentResponse contentResponse = httpClient.POST(account).content(new FormContentProvider(fields)).send();

            if(contentResponse.getStatus() != HttpServletResponse.SC_OK) {
                log.error("create account error {}, {}", contentResponse.getStatus(), contentResponse.getContentAsString());
                throw new IllegalStateException(contentResponse.getContentAsString());
            }

            final String response = contentResponse.getContentAsString();

            Assert.assertNotNull(response);

            log.info(response);

            return Account.fromJson(response);

        } finally {
            httpClient.stop();
        }
    }

    public static @Nullable Account restGetAccount(final long id) throws Exception {

        @Nonnull final HttpClient httpClient = new HttpClient();

        try {

            httpClient.start();

            final ContentResponse contentResponse = httpClient.GET(account + "/" + id);

            if(contentResponse.getStatus() == HttpServletResponse.SC_NOT_FOUND) return null;

            if(contentResponse.getStatus() != HttpServletResponse.SC_OK) {
                log.error("get account error {}, {}", contentResponse.getStatus(), contentResponse.getContentAsString());
                throw new IllegalStateException(contentResponse.getContentAsString());
            }

            final String response = contentResponse.getContentAsString();

            Assert.assertNotNull(response);

            log.info(response);

            return Account.fromJson(response);

        } finally {
            httpClient.stop();
        }
    }

}

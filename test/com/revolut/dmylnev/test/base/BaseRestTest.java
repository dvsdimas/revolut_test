package com.revolut.dmylnev.test.base;

import com.google.gson.Gson;
import com.revolut.dmylnev.business.exceptions.*;
import com.revolut.dmylnev.entity.Account;
import com.revolut.dmylnev.entity.Activity;
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
import java.util.*;

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
    private static final String deposit = base + "/deposit";
    private static final String withdrawal = base + "/withdrawal";
    private static final String transfer = base + "/transfer";

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

    public static @Nonnull Account restCreateAccount(@Nonnull final String currency) throws Exception {

        Objects.requireNonNull(currency);

        @Nonnull final HttpClient httpClient = new HttpClient();

        try {

            httpClient.start();

            @Nonnull final Fields fields = new Fields();

            fields.put(Account.PARAM_CURRENCY, currency);

            final ContentResponse contentResponse = httpClient.POST(account).content(new FormContentProvider(fields)).send();

            final String content = contentResponse.getContentAsString();
            Assert.assertNotNull(content);
            log.info(content);

            if(contentResponse.getStatus() != HttpServletResponse.SC_OK) handleErrors(contentResponse.getStatus(), content);

            return Account.fromJson(content);

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

            final String content = contentResponse.getContentAsString();
            Assert.assertNotNull(content);
            log.info(content);

            if(contentResponse.getStatus() != HttpServletResponse.SC_OK) handleErrors(contentResponse.getStatus(), content);

            return Account.fromJson(content);

        } finally {
            httpClient.stop();
        }
    }

    public static @Nonnull Activity restDepositAccount(final long id, @Nonnull final String currency, final double amount) throws Exception {

        Objects.requireNonNull(currency);

        @Nonnull final HttpClient httpClient = new HttpClient();

        try {

            httpClient.start();

            @Nonnull final Fields fields = new Fields();

            fields.put(Account.PARAM_CURRENCY, currency);
            fields.put(Account.PARAM_AMOUNT, String.valueOf(amount));

            final ContentResponse contentResponse = httpClient.POST(deposit + "/" + id).content(new FormContentProvider(fields)).send();

            final String content = contentResponse.getContentAsString();
            Assert.assertNotNull(content);
            log.info(content);

            if(contentResponse.getStatus() != HttpServletResponse.SC_OK) handleErrors(contentResponse.getStatus(), content);

            return Activity.fromJson(content);

        } finally {
            httpClient.stop();
        }
    }

    public static @Nonnull Activity restWithdrawalAccount(final long id, @Nonnull final String currency, final double amount) throws Exception {

        Objects.requireNonNull(currency);

        @Nonnull final HttpClient httpClient = new HttpClient();

        try {

            httpClient.start();

            @Nonnull final Fields fields = new Fields();

            fields.put(Account.PARAM_CURRENCY, currency);
            fields.put(Account.PARAM_AMOUNT, String.valueOf(amount));

            final ContentResponse contentResponse = httpClient.POST(withdrawal + "/" + id).content(new FormContentProvider(fields)).send();

            final String content = contentResponse.getContentAsString();

            Assert.assertNotNull(content);

            log.info(content);

            if(contentResponse.getStatus() != HttpServletResponse.SC_OK) handleErrors(contentResponse.getStatus(), content);

            return Activity.fromJson(content);

        } finally {
            httpClient.stop();
        }
    }

    public static @Nonnull List<Activity> restTransferAccounts(final long from, final long to, @Nonnull final String currency, final double amount) throws Exception {

        Objects.requireNonNull(currency);

        @Nonnull final HttpClient httpClient = new HttpClient();

        try {

            httpClient.start();

            @Nonnull final Fields fields = new Fields();

            fields.put(Account.PARAM_FROM, String.valueOf(from));
            fields.put(Account.PARAM_TO, String.valueOf(to));
            fields.put(Account.PARAM_CURRENCY, currency);
            fields.put(Account.PARAM_AMOUNT, String.valueOf(amount));

            final ContentResponse contentResponse = httpClient.POST(transfer).content(new FormContentProvider(fields)).send();

            final String content = contentResponse.getContentAsString();
            Assert.assertNotNull(content);
            log.info(content);

            if(contentResponse.getStatus() != HttpServletResponse.SC_OK) handleErrors(contentResponse.getStatus(), content);

            @Nonnull final Map<String, String> map = new Gson().fromJson(content, HashMap.class);

            @Nonnull final ArrayList<Activity> list = new ArrayList<>(2);

            list.add(0, Activity.fromJson(map.get("from")));
            list.add(1, Activity.fromJson(map.get("to")));

            return list;

        } finally {
            httpClient.stop();
        }
    }

    private static void handleErrors(final int status, @Nonnull final String content) throws BusinessException {

        if(status == HttpServletResponse.SC_CONFLICT) {
            if(content.contains(NotEnoughMoneyException.msg))      throw NotEnoughMoneyException.fromJson(content);
            if(content.contains(AccountNotFoundException.msg))     throw AccountNotFoundException.fromJson(content);
            if(content.contains(SameAccountTransferException.msg)) throw SameAccountTransferException.fromJson(content);
            if(content.contains(DifferentCurrenciesException.msg)) throw DifferentCurrenciesException.fromJson(content);
            if(content.contains(SmallAmountException.msg))         throw SmallAmountException.fromJson(content);
        }

        log.error("status {}, content: {}", status, content);

        throw new IllegalStateException(content);
    }

}

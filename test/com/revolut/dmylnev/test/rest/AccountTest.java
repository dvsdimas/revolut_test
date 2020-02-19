package com.revolut.dmylnev.test.rest;

import com.revolut.dmylnev.test.base.BaseDBTest;
import com.revolut.dmylnev.test.base.BaseRestTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.util.FormContentProvider;
import org.eclipse.jetty.util.Fields;
import org.junit.Assert;
import org.junit.Test;
import javax.annotation.Nonnull;

/**
 * @author dmylnev
 * @since 19.02.2020
 */

public class AccountTest extends BaseRestTest {

    private static final Logger log = LogManager.getLogger(BaseDBTest.class);

    @Test
    public void createAccount() throws Exception {

       @Nonnull final HttpClient httpClient = new HttpClient();

        httpClient.start();

        final String response = httpClient.GET("http://localhost:8080/account/1").getContentAsString();

        Assert.assertNotNull(response);


    }

    @Test
    public void createAccount2() throws Exception {

        @Nonnull final HttpClient httpClient = new HttpClient();

        httpClient.start();

        Fields fields = new Fields();
        fields.put("fruit", "apple");

        final String response = httpClient.POST("http://localhost:8080/account")
                .content(new FormContentProvider(fields))
                .send().getContentAsString();



        Assert.assertNotNull(response);


    }

}

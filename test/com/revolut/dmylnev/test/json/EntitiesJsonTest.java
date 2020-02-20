package com.revolut.dmylnev.test.json;

import com.google.gson.JsonSyntaxException;
import com.revolut.dmylnev.entity.Account;
import org.junit.Assert;
import org.junit.Test;
import javax.annotation.Nonnull;
import java.math.BigDecimal;

/**
 * @author dmylnev
 * @since 19.02.2020
 */

public class EntitiesJsonTest {

    @Test
    public void accountToJsonTest() {

        @Nonnull final Account account = new Account(1L, "USD", new BigDecimal("123.7"), 0L);

        @Nonnull final String json = account.toJson();
        @Nonnull final String str = account.toJson();

        Assert.assertEquals(json, str);

        @Nonnull final Account fromJson = Account.fromJson(json);

        Assert.assertNotNull(fromJson);
        Assert.assertEquals(account, fromJson);
    }

    @Test(expected = JsonSyntaxException.class)
    public void failTest() {
        Account.fromJson("json");
    }

    @Test(expected = IllegalArgumentException.class)
    public void failTest2() {
        Account.fromJson("{ \"status\": \"ok\"}");
    }

}


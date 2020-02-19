package com.revolut.dmylnev.test.json;

import com.revolut.dmylnev.entity.Account;
import org.junit.Assert;
import org.junit.Test;
import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author dmylnev
 * @since 19.02.2020
 */

public class EntitiesJsonTest {

    @Test
    public void accountToJsonTest() {

        @Nonnull final Long id = 1L;
        @Nonnull final String currency = "USD";
        @Nonnull final BigDecimal amount = new BigDecimal("123.7");
        @Nonnull final UUID uuid = UUID.randomUUID();

        @Nonnull final Account account = new Account(id, currency, amount, uuid);

        @Nonnull final String json = account.toJson();
        @Nonnull final String str = account.toJson();

        Assert.assertEquals(json, str);

        @Nonnull final Account fromJson = Account.fromJson(json);

        Assert.assertNotNull(fromJson);
        Assert.assertEquals(account, fromJson);
    }

}

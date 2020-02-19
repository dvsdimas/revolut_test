package com.revolut.dmylnev.test.rest;

import com.revolut.dmylnev.entity.Account;
import com.revolut.dmylnev.test.base.BaseDBTest;
import com.revolut.dmylnev.test.base.BaseRestTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @author dmylnev
 * @since 19.02.2020
 */

public class AccountTest extends BaseRestTest {

    private static final Logger log = LogManager.getLogger(BaseDBTest.class);

    @Test
    public void createAccount() throws Exception {

        @Nullable final Account nullAccount = restGetAccount(1);

        Assert.assertNull(nullAccount);

        //--------------------------------------------------------------------------------------------------------------

        @Nonnull final String currency = "USD";
        @Nonnull final UUID uuid = UUID.randomUUID();

        log.info("Creating account with currency [{}] and uuid [{}]", currency, uuid.toString());

        @Nonnull final Account createdAccount = restCreateAccount(currency, uuid);

//        Assert.assertNotNull(createdAccount);

        //--------------------------------------------------------------------------------------------------------------

        @Nullable final Account account = restGetAccount(1);

//        Assert.assertNotNull(account);

//        Assert.assertEquals(account, createdAccount);
    }

}

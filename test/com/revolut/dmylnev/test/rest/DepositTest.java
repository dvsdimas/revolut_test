package com.revolut.dmylnev.test.rest;

import com.revolut.dmylnev.entity.Account;
import com.revolut.dmylnev.entity.Activity;
import com.revolut.dmylnev.test.base.BaseRestTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import javax.annotation.Nonnull;

/**
 * @author dmylnev
 * @since 20.02.2020
 */

public class DepositTest extends BaseRestTest {

    private static final Logger log = LogManager.getLogger(DepositTest.class);

    @Test
    public void depositTest() throws Exception {

        @Nonnull final String currency = "USD";

        log.info("Creating account with currency [{}]", currency);

        @Nonnull final Account account = restCreateAccount(currency);

        Assert.assertNotNull(account);

        //--------------------------------------------------------------------------------------------------------------

        final double amount = 3.33;

        @Nonnull final Activity activity = restDepositAccount(account.id, account.currency, amount);

        Assert.assertNotNull(activity);

        // todo

        //--------------------------------------------------------------------------------------------------------------

        // todo account check

    }

    // todo test amount

    // todo test account id

}

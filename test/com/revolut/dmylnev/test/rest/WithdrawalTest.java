package com.revolut.dmylnev.test.rest;

import com.revolut.dmylnev.entity.Account;
import com.revolut.dmylnev.entity.Activity;
import com.revolut.dmylnev.entity.ActivityType;
import com.revolut.dmylnev.test.base.BaseRestTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author dmylnev
 * @since 20.02.2020
 */

public class WithdrawalTest extends BaseRestTest {

    private static final Logger log = LogManager.getLogger(WithdrawalTest.class);

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

        Assert.assertEquals(1, activity.id);
        Assert.assertEquals(account.id, activity.account);
        Assert.assertEquals(account.currency, activity.currency);
        Assert.assertEquals(amount, activity.amount, DELTA);
        Assert.assertEquals(ActivityType.DEPOSIT, activity.type);
        Assert.assertNull(activity.counterpart);

        //--------------------------------------------------------------------------------------------------------------

        @Nullable final Account accountAfter = restGetAccount(account.id);

        Assert.assertNotNull(accountAfter);

        Assert.assertEquals(account.id, accountAfter.id);
        Assert.assertEquals(amount, accountAfter.amount, DELTA);
        Assert.assertEquals(activity.id, accountAfter.version);

        //--------------------------------------------------------------------------------------------------------------

        final double amountWithdrawal = .34;

        @Nonnull final Activity activityW = restWithdrawalAccount(account.id, account.currency, amountWithdrawal);

        Assert.assertNotNull(activityW);

        Assert.assertEquals(2, activityW.id);
        Assert.assertEquals(account.id, activityW.account);
        Assert.assertEquals(account.currency, activityW.currency);
        Assert.assertEquals(-amountWithdrawal, activityW.amount, DELTA);
        Assert.assertEquals(ActivityType.WITHDRAWAL, activityW.type);
        Assert.assertNull(activityW.counterpart);

        //--------------------------------------------------------------------------------------------------------------

        @Nullable final Account accountAfter2 = restGetAccount(account.id);

        Assert.assertNotNull(accountAfter2);

        Assert.assertEquals(account.id, accountAfter2.id);
        Assert.assertEquals(amount - amountWithdrawal, accountAfter2.amount, DELTA);
        Assert.assertEquals(activityW.id, accountAfter2.version);
    }

    @Test(expected = IllegalStateException.class)
    public void depositFail1Test() throws Exception {

        final double amount = 3.33;

        @Nonnull final Activity activity = restDepositAccount(43245, "RUB", amount);

        Assert.assertNotNull(activity);
    }

    @Test(expected = IllegalStateException.class)
    public void depositFail2Test() throws Exception {

        @Nonnull final String currency = "USD";

        log.info("Creating account with currency [{}]", currency);

        @Nonnull final Account account = restCreateAccount(currency);

        Assert.assertNotNull(account);

        //--------------------------------------------------------------------------------------------------------------

        @Nonnull final Activity activity = restDepositAccount(account.id, account.currency, -3.33);

        Assert.assertNotNull(activity);
    }

    @Test(expected = IllegalStateException.class)
    public void depositFail3Test() throws Exception {

        @Nonnull final String currency = "USD";

        log.info("Creating account with currency [{}]", currency);

        @Nonnull final Account account = restCreateAccount(currency);

        Assert.assertNotNull(account);

        //--------------------------------------------------------------------------------------------------------------

        @Nonnull final Activity activity = restDepositAccount(account.id, account.currency, 0.001);

        Assert.assertNotNull(activity);
    }

}
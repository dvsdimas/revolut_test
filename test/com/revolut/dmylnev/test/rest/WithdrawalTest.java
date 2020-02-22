package com.revolut.dmylnev.test.rest;

import com.revolut.dmylnev.business.exceptions.AccountNotFoundException;
import com.revolut.dmylnev.business.exceptions.DifferentCurrenciesException;
import com.revolut.dmylnev.business.exceptions.NotEnoughMoneyException;
import com.revolut.dmylnev.business.exceptions.SmallAmountException;
import com.revolut.dmylnev.entity.Account;
import com.revolut.dmylnev.entity.Activity;
import com.revolut.dmylnev.entity.ActivityType;
import com.revolut.dmylnev.test.base.BaseRestTest;
import org.junit.Assert;
import org.junit.Test;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author dmylnev
 * @since 20.02.2020
 */

public class WithdrawalTest extends BaseRestTest {

    @Test
    public void withdrawalTest() throws Exception {

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
    public void withdrawalFail2Test() throws Exception {

        @Nonnull final Account account = restCreateAccount(currency);

        Assert.assertNotNull(account);

        //--------------------------------------------------------------------------------------------------------------

        @Nonnull final Activity activity = restDepositAccount(account.id, account.currency, -3.33);

        Assert.assertNotNull(activity);
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void withdrawalNotEnoughMoneyTest() throws Exception {

        @Nonnull final Account account = restCreateAccount(currency);

        Assert.assertNotNull(account);

        //--------------------------------------------------------------------------------------------------------------

        final double amount = .01;

        @Nonnull final Activity activity = restDepositAccount(account.id, account.currency, amount);

        Assert.assertNotNull(activity);

        Assert.assertEquals(amount, activity.amount, DELTA);

        //--------------------------------------------------------------------------------------------------------------

        @Nullable final Account accountAfter = restGetAccount(account.id);

        Assert.assertNotNull(accountAfter);

        Assert.assertEquals(amount, accountAfter.amount, DELTA);

        //--------------------------------------------------------------------------------------------------------------

        final double amountWithdrawal = .2;

        restWithdrawalAccount(account.id, account.currency, amountWithdrawal);
    }

    @Test(expected = AccountNotFoundException.class)
    public void withdrawalAccountNotFoundTest() throws Exception {
        restWithdrawalAccount(32324892, currency, 1d);
    }

    @Test(expected = DifferentCurrenciesException.class)
    public void withdrawalWrongCurrencyTest() throws Exception {

        @Nonnull final Account account = restCreateAccount(currency);

        Assert.assertNotNull(account);
        Assert.assertEquals(currency, account.currency);

        //--------------------------------------------------------------------------------------------------------------

        restWithdrawalAccount(account.id, "EUR", 1_000_000);
    }

    @Test(expected = SmallAmountException.class)
    public void withdrawalSmallAmountTest() throws Exception {

        @Nonnull final Account account = restCreateAccount(currency);

        Assert.assertNotNull(account);
        Assert.assertEquals(currency, account.currency);

        //--------------------------------------------------------------------------------------------------------------

        restWithdrawalAccount(account.id, currency, 0.001);
    }

}

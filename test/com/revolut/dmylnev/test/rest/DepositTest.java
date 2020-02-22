package com.revolut.dmylnev.test.rest;

import com.revolut.dmylnev.business.exceptions.AccountNotFoundException;
import com.revolut.dmylnev.business.exceptions.DifferentCurrenciesException;
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

public class DepositTest extends BaseRestTest {

    @Test
    public void depositTest() throws Exception {

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
    }

    @Test(expected = IllegalStateException.class)
    public void depositFail2Test() throws Exception {

        @Nonnull final Account account = restCreateAccount(currency);

        Assert.assertNotNull(account);

        //--------------------------------------------------------------------------------------------------------------

        @Nonnull final Activity activity = restDepositAccount(account.id, account.currency, -3.33);

        Assert.assertNotNull(activity);
    }

    @Test(expected = AccountNotFoundException.class)
    public void depositAccountNotFoundTest() throws Exception {
        restDepositAccount(3232323, currency, 1d);
    }

    @Test(expected = DifferentCurrenciesException.class)
    public void depositWrongCurrencyTest() throws Exception {

        @Nonnull final Account account = restCreateAccount(currency);

        Assert.assertNotNull(account);
        Assert.assertEquals(currency, account.currency);

        //--------------------------------------------------------------------------------------------------------------

        restDepositAccount(account.id, "EUR", 1_000_000);
    }

    @Test(expected = SmallAmountException.class)
    public void depositSmallAmountTest() throws Exception {

        @Nonnull final Account account = restCreateAccount(currency);

        Assert.assertNotNull(account);
        Assert.assertEquals(currency, account.currency);

        //--------------------------------------------------------------------------------------------------------------

        restDepositAccount(account.id, currency, 0.001);
    }

}

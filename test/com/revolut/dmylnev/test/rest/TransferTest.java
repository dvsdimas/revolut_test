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
import java.util.List;

/**
 * @author dmylnev
 * @since 22.02.2020
 */

public class TransferTest extends BaseRestTest {

    private static final Logger log = LogManager.getLogger(TransferTest.class);

    @Test
    public void transferTest() throws Exception {

        @Nonnull final String currency = "USD";

        @Nonnull final Account account = restCreateAccount(currency);

        Assert.assertNotNull(account);
        Assert.assertEquals(1, account.id);
        Assert.assertEquals(currency, account.currency);

        //--------------------------------------------------------------------------------------------------------------

        final double amount = 100.01;

        @Nonnull final Activity activity = restDepositAccount(account.id, account.currency, amount);

        Assert.assertNotNull(activity);

        Assert.assertEquals(1, activity.id);
        Assert.assertEquals(account.id, activity.account);
        Assert.assertEquals(account.currency, activity.currency);
        Assert.assertEquals(amount, activity.amount, DELTA);
        Assert.assertEquals(ActivityType.DEPOSIT, activity.type);
        Assert.assertNull(activity.counterpart);

        //--------------------------------------------------------------------------------------------------------------

        @Nullable final Account accountFrom = restGetAccount(account.id);

        Assert.assertNotNull(accountFrom);

        Assert.assertEquals(account.id, accountFrom.id);
        Assert.assertEquals(amount, accountFrom.amount, DELTA);
        Assert.assertEquals(activity.id, accountFrom.version);

        //--------------------------------------------------------------------------------------------------------------

        @Nonnull final Account accountTo = restCreateAccount(currency);

        Assert.assertNotNull(accountTo);
        Assert.assertEquals(2, accountTo.id);
        Assert.assertEquals(currency, accountTo.currency);
        Assert.assertEquals(0, accountTo.amount, DELTA);

        //--------------------------------------------------------------------------------------------------------------

        @Nonnull final List<Activity> activities = restTransferAccount(account.id, accountTo.id, account.currency, amount);

        Assert.assertNotNull(activities);

        final Activity activityFrom = activities.get(0);
        final Activity activityTo = activities.get(1);

        Assert.assertNotNull(activityFrom);
        Assert.assertNotNull(activityTo);

        Assert.assertNotEquals(activityTo.id, activityFrom.id);

        Assert.assertEquals(currency, activityFrom.currency);
        Assert.assertEquals(currency, activityTo.currency);

        Assert.assertEquals(ActivityType.TRANSFER_FROM, activityFrom.type);
        Assert.assertEquals(ActivityType.TRANSFER_TO, activityTo.type);

        Assert.assertEquals(-amount, activityFrom.amount, DELTA);
        Assert.assertEquals(amount, activityTo.amount, DELTA);

        Assert.assertEquals(accountFrom.id, activityFrom.account);
        Assert.assertEquals(accountTo.id, activityTo.account);

        Assert.assertEquals(accountTo.id, activityFrom.counterpart.longValue());
        Assert.assertEquals(accountFrom.id, activityTo.counterpart.longValue());

        //--------------------------------------------------------------------------------------------------------------

        @Nullable final Account accountFromAfter = restGetAccount(accountFrom.id);
        @Nullable final Account accountToAfter = restGetAccount(accountTo.id);

        Assert.assertNotNull(accountFromAfter);
        Assert.assertNotNull(accountToAfter);

        Assert.assertEquals(0, accountFromAfter.amount, DELTA);
        Assert.assertEquals(amount, accountToAfter.amount, DELTA);

        Assert.assertEquals(activityFrom.id, accountFromAfter.version);
        Assert.assertEquals(activityTo.id, accountToAfter.version);
    }
}

package com.revolut.dmylnev.test.services;

import com.revolut.dmylnev.entity.Account;
import com.revolut.dmylnev.entity.Activity;
import com.revolut.dmylnev.entity.ActivityType;
import com.revolut.dmylnev.services.IAccountService;
import com.revolut.dmylnev.services.ServicesProvider;
import com.revolut.dmylnev.test.base.BaseDBTest;
import org.junit.Assert;
import org.junit.Test;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * @author dmylnev
 * @since 22.02.2020
 */

public class AccountServiceTest extends BaseDBTest {

    private final @Nonnull IAccountService accountService = Objects.requireNonNull(ServicesProvider.getAccountService());

    @Test(expected = IllegalArgumentException.class)
    public void createAccountNullCurrency() throws Exception {
        accountService.createAccount(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAccountEmptyCurrency() throws Exception {
        accountService.createAccount("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAccountBigCurrency() throws Exception {
        accountService.createAccount("01234567890");
    }

    @Test
    public void complexTest() throws Exception {

        test("RUB", .01);

        test("GBP", .11);

        test("USD", 999.99);

        test("EUR", 1.0);

        test("ZAR", 1_000_000_000_000.01);

        test("0123456789", 1.0);
    }

    private void test(@Nonnull final String cur, final double amount) throws Exception {

        @Nonnull final Account accountFrom = accountService.createAccount(cur);
        @Nonnull final Account accountTo = accountService.createAccount(cur);

        Assert.assertNotNull(accountFrom);
        Assert.assertNotNull(accountTo);

        Assert.assertEquals(cur, accountFrom.currency);
        Assert.assertEquals(cur, accountTo.currency);

        //--------------------------------------------------------------------------------------------------------------

        @Nonnull final Activity deposit = accountService.deposit(accountFrom.id, cur, amount);

        Assert.assertNotNull(deposit);

        Assert.assertEquals(accountFrom.id, deposit.account);
        Assert.assertEquals(accountFrom.currency, deposit.currency);
        Assert.assertEquals(ActivityType.DEPOSIT, deposit.type);
        Assert.assertEquals(amount, deposit.amount, DELTA);
        Assert.assertNull(deposit.counterpart);
        Assert.assertEquals(deposit.amount, accountService.recalculateBalanceByActivities(accountFrom.id), DELTA);

        //--------------------------------------------------------------------------------------------------------------

        @Nonnull final List<Activity> transfers = accountService.transfer(accountFrom.id, accountTo.id, cur, amount);

        Assert.assertNotNull(transfers);

        @Nonnull final Activity transferFrom = transfers.get(0);
        @Nonnull final Activity transferTo = transfers.get(1);

        Assert.assertNotNull(transferFrom);
        Assert.assertNotNull(transferTo);

        Assert.assertEquals(accountFrom.id, transferFrom.account);
        Assert.assertEquals(accountFrom.currency, transferFrom.currency);
        Assert.assertEquals(ActivityType.TRANSFER_FROM, transferFrom.type);
        Assert.assertEquals(-amount, transferFrom.amount, DELTA);
        Assert.assertEquals(accountTo.id, transferFrom.counterpart.longValue());
        Assert.assertEquals(0, accountService.recalculateBalanceByActivities(accountFrom.id), DELTA);

        Assert.assertEquals(accountTo.id, transferTo.account);
        Assert.assertEquals(accountTo.currency, transferTo.currency);
        Assert.assertEquals(ActivityType.TRANSFER_TO, transferTo.type);
        Assert.assertEquals(amount, transferTo.amount, DELTA);
        Assert.assertEquals(accountFrom.id, transferTo.counterpart.longValue());
        Assert.assertEquals(amount, accountService.recalculateBalanceByActivities(accountTo.id), DELTA);

        //--------------------------------------------------------------------------------------------------------------

        @Nonnull final Activity withdrawal = accountService.withdrawal(accountTo.id, cur, amount);

        Assert.assertNotNull(withdrawal);

        Assert.assertEquals(accountTo.id, withdrawal.account);
        Assert.assertEquals(accountTo.currency, withdrawal.currency);
        Assert.assertEquals(ActivityType.WITHDRAWAL, withdrawal.type);
        Assert.assertEquals(-amount, withdrawal.amount, DELTA);
        Assert.assertNull(withdrawal.counterpart);
        Assert.assertEquals(0, accountService.recalculateBalanceByActivities(accountTo.id), DELTA);

        //--------------------------------------------------------------------------------------------------------------

        @Nullable final Account accountFromEnd = accountService.getAccount(accountFrom.id);
        @Nullable final Account accountToEnd = accountService.getAccount(accountTo.id);

        Assert.assertNotNull(accountFromEnd);
        Assert.assertNotNull(accountToEnd);

        Assert.assertEquals(accountFrom.id, accountFromEnd.id);
        Assert.assertEquals(accountFrom.currency, accountFromEnd.currency);
        Assert.assertEquals(0, accountFromEnd.amount, DELTA);
        Assert.assertNotEquals(accountFrom.version, accountFromEnd.version);

        Assert.assertEquals(accountTo.id, accountToEnd.id);
        Assert.assertEquals(accountTo.currency, accountToEnd.currency);
        Assert.assertEquals(0, accountToEnd.amount, DELTA);
        Assert.assertNotEquals(accountTo.version, accountToEnd.version);
    }

}

package com.revolut.dmylnev.test.services;

import com.revolut.dmylnev.entity.Account;
import com.revolut.dmylnev.services.IAccountService;
import com.revolut.dmylnev.services.ServicesProvider;
import com.revolut.dmylnev.test.base.BaseDBTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * @author dmylnev
 * @since 23.02.2020
 */

public class AccountServiceTransactionsTest extends BaseDBTest {

    private static final Logger log = LogManager.getLogger(AccountServiceTransactionsTest.class);

    private final @Nonnull IAccountService accountService = Objects.requireNonNull(ServicesProvider.getAccountService());

    @Test(timeout = 20_000)
    public void test() throws Exception {

        final double amount = 0.01;
        final int count = 10;
        final int size = 1000;

        @Nonnull final Account accountFrom = accountService.createAccount(currency);
        @Nonnull final Account accountTo = accountService.createAccount(currency);

        Assert.assertNotNull(accountFrom);
        Assert.assertNotNull(accountTo);

        Assert.assertEquals(currency, accountFrom.currency);
        Assert.assertEquals(currency, accountTo.currency);

        //--------------------------------------------------------------------------------------------------------------

        @Nonnull final CountDownLatch latch1 = new CountDownLatch(count);
        @Nonnull final CountDownLatch latch2 = new CountDownLatch(1);
        @Nonnull final CountDownLatch latch3 = new CountDownLatch(count);
        @Nonnull final CountDownLatch latch4 = new CountDownLatch(1);
        @Nonnull final CountDownLatch latch5 = new CountDownLatch(count);
        @Nonnull final CountDownLatch latch6 = new CountDownLatch(1);
        @Nonnull final CountDownLatch latch7 = new CountDownLatch(count);

        @Nonnull final Thread[] threads = new Thread[count];

        for(int i = 0; i < count; i++) {

            threads[i] = new Thread(() -> {

                try {

                    latch1.countDown();
                    latch2.await();

                    for(int j = 0; j < size; j++) accountService.deposit(accountFrom.id, currency, amount);

                    latch3.countDown();
                    latch4.await();

                    for(int j = 0; j < size; j++) accountService.transfer(accountFrom.id, accountTo.id, currency, amount);

                    latch5.countDown();
                    latch6.await();

                    for(int j = 0; j < size; j++) accountService.withdrawal(accountTo.id, currency, amount);

                    latch7.countDown();

                } catch (Throwable th) {
                    log.error("Thread error", th);
                }
            });

            threads[i].start();
        }

        latch1.await();
        latch2.countDown();
        latch3.await();

        Assert.assertEquals(amount * count * size, accountService.getAccount(accountFrom.id).amount, DELTA);
        Assert.assertEquals(count * size, accountService.getAccount(accountFrom.id).version);
        Assert.assertEquals(amount * count * size, accountService.recalculateBalanceByActivities(accountFrom.id), DELTA);

        Assert.assertEquals(0, accountService.getAccount(accountTo.id).amount, DELTA);
        Assert.assertEquals(0, accountService.getAccount(accountTo.id).version);
        Assert.assertEquals(0, accountService.recalculateBalanceByActivities(accountTo.id), DELTA);

        latch4.countDown();
        latch5.await();

        Assert.assertEquals(0, accountService.getAccount(accountFrom.id).amount, DELTA);
        Assert.assertEquals(3 * count * size - 1, accountService.getAccount(accountFrom.id).version);
        Assert.assertEquals(0, accountService.recalculateBalanceByActivities(accountFrom.id), DELTA);

        Assert.assertEquals(amount * count * size, accountService.getAccount(accountTo.id).amount, DELTA);
        Assert.assertEquals(3 * count * size, accountService.getAccount(accountTo.id).version);
        Assert.assertEquals(amount * count * size, accountService.recalculateBalanceByActivities(accountTo.id), DELTA);

        latch6.countDown();
        latch7.await();

        Assert.assertEquals(0, accountService.getAccount(accountFrom.id).amount, DELTA);
        Assert.assertEquals(3 * count * size - 1, accountService.getAccount(accountFrom.id).version);
        Assert.assertEquals(0, accountService.recalculateBalanceByActivities(accountFrom.id), DELTA);

        Assert.assertEquals(0, accountService.getAccount(accountTo.id).amount, DELTA);
        Assert.assertEquals(4 * count * size, accountService.getAccount(accountTo.id).version);
        Assert.assertEquals(0, accountService.recalculateBalanceByActivities(accountTo.id), DELTA);
    }

}

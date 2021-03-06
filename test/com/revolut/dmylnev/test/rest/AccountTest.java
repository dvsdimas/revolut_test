package com.revolut.dmylnev.test.rest;

import com.revolut.dmylnev.entity.Account;
import com.revolut.dmylnev.services.ServicesProvider;
import com.revolut.dmylnev.test.base.BaseRestTest;
import org.junit.Assert;
import org.junit.Test;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author dmylnev
 * @since 19.02.2020
 */

public class AccountTest extends BaseRestTest {

    @Test
    public void createAccount() throws Exception {

        @Nullable final Account nullAccount = restGetAccount(1);

        Assert.assertNull(nullAccount);

        //--------------------------------------------------------------------------------------------------------------

        @Nonnull final Account createdAccount = restCreateAccount(currency);

        Assert.assertNotNull(createdAccount);
        Assert.assertEquals(currency, createdAccount.currency);

        //--------------------------------------------------------------------------------------------------------------

        @Nullable final Account account = restGetAccount(1);

        Assert.assertNotNull(account);

        Assert.assertEquals(account, createdAccount);

        //--------------------------------------------------------------------------------------------------------------

        @Nullable final Account accountService = ServicesProvider.getAccountService().getAccount(1L);

        Assert.assertNotNull(accountService);

        Assert.assertEquals(accountService, createdAccount);
    }

}

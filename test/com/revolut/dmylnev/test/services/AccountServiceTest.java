package com.revolut.dmylnev.test.services;

import com.revolut.dmylnev.entity.Account;
import com.revolut.dmylnev.services.IAccountService;
import com.revolut.dmylnev.services.ServicesProvider;
import com.revolut.dmylnev.test.base.BaseDBTest;
import org.junit.Assert;
import org.junit.Test;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author dmylnev
 * @since 22.02.2020
 */

public class AccountServiceTest extends BaseDBTest {

    private final @Nonnull IAccountService accountService = Objects.requireNonNull(ServicesProvider.getAccountService());

    @Test
    public void createAccount() throws Exception {

        @Nullable final Account nullAccount = accountService.getAccount(1L);

        Assert.assertNull(nullAccount);

        //--------------------------------------------------------------------------------------------------------------

        @Nonnull final Account createdAccount = accountService.createAccount(currency);

        Assert.assertNotNull(createdAccount);
        Assert.assertEquals(currency, createdAccount.currency);

        //--------------------------------------------------------------------------------------------------------------

        @Nullable final Account account = accountService.getAccount(1L);

        Assert.assertNotNull(account);

        Assert.assertEquals(account, createdAccount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAccountNullCurrency() throws Exception {
        accountService.createAccount(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAccountEmptyCurrency() throws Exception {
        accountService.createAccount("");
    }

}

package com.revolut.dmylnev.services;

import com.revolut.dmylnev.database.DbConnectionProvider;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author dmylnev
 * @since 19.02.2020
 */

public class ServicesProvider {

    private static @Nonnull final AtomicReference<IAccountService> accountService = new AtomicReference<>();

    private ServicesProvider() { throw new IllegalStateException(); }

    public static void init(@Nonnull final DbConnectionProvider dbConnectionProvider) {
        accountService.set(new AccountServiceImpl(dbConnectionProvider));
    }

    public static @Nonnull IAccountService getAccountService() {

        @Nullable IAccountService as = accountService.get();

        if(as == null) throw new IllegalStateException("Not init ServicesProvider");

        return as;
    }

}

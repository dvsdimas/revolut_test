package com.revolut.dmylnev.services;

import com.revolut.dmylnev.database.DbConnectionProvider;
import com.revolut.dmylnev.entity.Account;
import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * @author dmylnev
 * @since 19.02.2020
 */

public class AccountServiceImpl extends BaseService implements IAccountService {

    public AccountServiceImpl(@Nonnull DbConnectionProvider dbConnectionProvider) {
        super(dbConnectionProvider);
    }

    @Override
    public @Nonnull Account createAccount(@Nonnull String currency, @Nonnull UUID uuid) {



        return null;
    }

    @Override
    public @Nonnull Account getAccount(@Nonnull Long id) {
        return null;
    }
}

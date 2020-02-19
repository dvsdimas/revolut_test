package com.revolut.dmylnev.services;

import com.revolut.dmylnev.database.DbConnectionProvider;
import com.revolut.dmylnev.entity.Account;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author dmylnev
 * @since 19.02.2020
 */

public class AccountServiceImpl extends BaseService implements IAccountService {

    public AccountServiceImpl(@Nonnull DbConnectionProvider dbConnectionProvider) {
        super(dbConnectionProvider);
    }

    @Override
    public @Nonnull Account createAccount(@Nonnull String currency) {



        return null;
    }

    @Override
    public @Nullable Account getAccount(@Nonnull Long id) throws SQLException {

        @Nonnull final Connection con = dbConnectionProvider.getConnection();



        return null;
    }
}

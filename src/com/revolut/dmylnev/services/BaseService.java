package com.revolut.dmylnev.services;

import com.revolut.dmylnev.database.DbConnectionProvider;
import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author dmylnev
 * @since 19.02.2020
 */

public abstract class BaseService {

    protected @Nonnull final DbConnectionProvider dbConnectionProvider;

    public BaseService(@Nonnull final DbConnectionProvider dbConnectionProvider) {
        this.dbConnectionProvider = Objects.requireNonNull(dbConnectionProvider);
    }
}

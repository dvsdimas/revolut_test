package com.revolut.dmylnev.database;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author dmylnev
 * @since 18.02.2020
 */

public final class DbConnectionProviderFactory {

    private static volatile DbConnectionProvider provider;

    private DbConnectionProviderFactory() { throw new IllegalStateException(); }

    public static synchronized @Nonnull DbConnectionProvider getProvider() {

        if(provider == null) throw new IllegalStateException("DbConnectionProviderFactory hasn't been init");

        return provider;
    }

    public static synchronized void init(@Nonnull final DbConnectionProvider provider) {
        DbConnectionProviderFactory.provider = Objects.requireNonNull(provider);
    }

}

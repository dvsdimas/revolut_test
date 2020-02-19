package com.revolut.dmylnev.database;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author dmylnev
 * @since 18.02.2020
 */

public final class DbConnectionProviderFactory {

    private static @Nonnull final AtomicReference<DbConnectionProvider> provider = new AtomicReference<>();

    private DbConnectionProviderFactory() { throw new IllegalStateException(); }

    public static synchronized @Nonnull DbConnectionProvider getProvider() {

        @Nullable final DbConnectionProvider pr = provider.get();

        if(pr == null) throw new IllegalStateException("DbConnectionProviderFactory hasn't been init");

        return pr;
    }

    public static synchronized void init(@Nonnull final DbConnectionProvider provider) {
        DbConnectionProviderFactory.provider.set(Objects.requireNonNull(provider));
    }

}

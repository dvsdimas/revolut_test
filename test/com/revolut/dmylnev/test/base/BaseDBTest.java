package com.revolut.dmylnev.test.base;

import com.revolut.dmylnev.database.DbConnectionProvider;
import com.revolut.dmylnev.database.h2.H2ConnectionProvider;
import com.revolut.dmylnev.services.ServicesProvider;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import javax.annotation.Nonnull;

/**
 * @author dmylnev
 * @since 17.02.2020
 */

public abstract class BaseDBTest {

    public static final double DELTA = 0.0000001;

    private static @Nonnull final DbConnectionProvider dbProvider = new H2ConnectionProvider("test", "user", "password");

    @BeforeClass
    public static void init() throws Exception {

        dbProvider.init();

        ServicesProvider.init(dbProvider);
    }

    @AfterClass
    public static void shutdown() throws Exception {

        dbProvider.shutdown();
    }

}

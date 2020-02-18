package com.revolut.dmylnev.test;

import com.revolut.dmylnev.database.DbConnectionProvider;
import com.revolut.dmylnev.database.h2.H2ConnectionProvider;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author dmylnev
 * @since 17.02.2020
 */

public abstract class BaseDbTest {

    private static @Nonnull final DbConnectionProvider dbProvider = new H2ConnectionProvider("test", "user", "password");

    @BeforeClass
    public static void init() throws SQLException, IOException {
        dbProvider.init();
    }

    @AfterClass
    public static void shutdown() {
        dbProvider.shutdown();
    }

}

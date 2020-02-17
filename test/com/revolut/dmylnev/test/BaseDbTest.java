package com.revolut.dmylnev.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author dmylnev
 * @since 17.02.2020
 */

public abstract class BaseDbTest {

    private static final Logger log = LogManager.getLogger(BaseDbTest.class);

    private static @Nullable volatile Connection connection = null;

    @BeforeClass
    public static void init() throws SQLException, IOException {

        connection = DriverManager.getConnection("jdbc:h2:mem:test1;MODE=PostgreSQL;LOCK_TIMEOUT=10000;", "user", "password");

        Assert.assertNotNull(connection);

        executeUpdate("schema_h2.sql");
    }

    private static void executeUpdate(final String resourcePath) throws SQLException, IOException {

        @Nonnull final StringBuilder sql = new StringBuilder();

        final String lineSeparator = System.getProperty("line.separator");

        try {

            @Nullable final URL url = BaseDbTest.class.getClassLoader().getResource(resourcePath);

            Assert.assertNotNull(url);

            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {

                String s;

                while ((s = reader.readLine()) != null) {
                    sql.append(s).append(lineSeparator);
                }
            }

        } catch (IOException e) {
            log.error(e);
            throw e;
        }

        try (@Nonnull final Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql.toString());
        }

    }

    @AfterClass
    public static void deinit() throws SQLException {

        try {

            if (connection == null) return;

            try (@Nonnull final Statement statement = connection.createStatement()) {
                statement.execute("SHUTDOWN");
            }

            connection.close();

            connection = null;

        } catch (Throwable e) {
            log.warn("H2 shutdown error " + e.getMessage(), e);
            throw e;
        }

    }

}

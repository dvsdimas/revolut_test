package com.revolut.dmylnev.database.h2;

import com.revolut.dmylnev.database.DbConnectionProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.Objects;

/**
 * @author dmylnev
 * @since 18.02.2020
 */

public final class H2ConnectionProvider implements DbConnectionProvider {

    private static final Logger log = LogManager.getLogger(H2ConnectionProvider.class);

    private static final String part1 = "jdbc:h2:mem:";
    private static final String part2 = ";MODE=PostgreSQL;LOCK_TIMEOUT=10000;";

    private @Nullable volatile Connection connection = null;

    private @Nonnull final String jdbcUrl;
    private @Nonnull final String login;
    private @Nonnull final String password;

    public H2ConnectionProvider(@Nonnull final String dbName, @Nonnull final String login, @Nonnull final String password) {
        this.jdbcUrl = part1 + Objects.requireNonNull(dbName) + part2;
        this.login = Objects.requireNonNull(login);
        this.password = Objects.requireNonNull(password);
    }

    @Override
    public synchronized void init() throws SQLException, IOException {

        if(connection != null) return;

        connection = getConnection();

        executeUpdate(connection,"schema_h2.sql");
    }

    @Override
    public synchronized void shutdown() {

        if(connection == null) return;

        try {

            try (@Nonnull final Statement statement = Objects.requireNonNull(connection.createStatement()) ) {
                statement.execute("SHUTDOWN");
            }

            connection.close();

        } catch (Throwable e) {
            log.warn("H2 shutdown error " + e.getMessage(), e);
        }

        connection = null;

    }

    @Override
    public @Nonnull Connection getConnection() throws SQLException {

        @Nonnull Connection connection = Objects.requireNonNull(DriverManager.getConnection(jdbcUrl, login, password));

        connection.setAutoCommit(false);

        return connection;
    }

    @Override
    public void releaseConnection(@Nonnull final Connection connection) {
        closeConnection(connection);
    }

    private void closeConnection(@Nonnull final Connection connection) {

        try {
            connection.close();
        } catch (SQLException e) {
            log.error("closeConnection error", e);
        }

    }

    private void executeUpdate(@Nonnull final Connection con, @Nonnull final String resourcePath) throws SQLException, IOException {

        Objects.requireNonNull(con);
        Objects.requireNonNull(resourcePath);

        @Nonnull final StringBuilder sql = new StringBuilder();

        final String lineSeparator = System.getProperty("line.separator");

        try {

            @Nullable final URL url = H2ConnectionProvider.class.getClassLoader().getResource(resourcePath);

            if(url == null) throw new IllegalStateException("Cannot find resource [" + resourcePath + "]");

            try (final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {

                String s;

                while ((s = reader.readLine()) != null) {
                    sql.append(s).append(lineSeparator);
                }
            }

        } catch (IOException e) {
            log.error("executeUpdate error", e);
            throw e;
        }

        try {

            Objects.requireNonNull(con);

            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            try (@Nonnull final Statement statement = Objects.requireNonNull(con.createStatement())) {
                statement.executeUpdate(sql.toString());
            }

            con.commit();

        } catch (Throwable th) {
            log.error("executeUpdate error", th);
            con.rollback();
        }

    }
}

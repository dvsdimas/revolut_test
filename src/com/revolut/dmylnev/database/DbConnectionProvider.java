package com.revolut.dmylnev.database;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author dmylnev
 * @since 18.02.2020
 */

public interface DbConnectionProvider {

    void init() throws SQLException, IOException;

    void shutdown();

    @Nonnull Connection getConnection() throws SQLException;

    void releaseConnection(@Nonnull final Connection connection);

}

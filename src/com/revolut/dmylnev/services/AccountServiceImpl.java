package com.revolut.dmylnev.services;

import com.revolut.dmylnev.database.DbConnectionProvider;
import com.revolut.dmylnev.entity.Account;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.sql.*;

/**
 * @author dmylnev
 * @since 19.02.2020
 */

@SuppressFBWarnings({"OBL_UNSATISFIED_OBLIGATION_EXCEPTION_EDGE", "UTWR_USE_TRY_WITH_RESOURCES"})
public class AccountServiceImpl extends BaseService implements IAccountService {

    private static final Logger log = LogManager.getLogger(AccountServiceImpl.class);

    private static @Nonnull final String getByIdSql = "SELECT id, currency, amount FROM accounts WHERE id = ?";
    private static @Nonnull final String createSql = "INSERT INTO accounts (currency) VALUES (?)";

    public AccountServiceImpl(@Nonnull DbConnectionProvider dbConnectionProvider) {
        super(dbConnectionProvider);
    }

    @Override
    public @Nonnull Account createAccount(@Nonnull String currency) throws SQLException {

        @Nonnull final Connection con = dbConnectionProvider.getConnection();

        @Nullable Account account = null;
        @Nullable PreparedStatement statement = null;
        @Nullable  ResultSet resultSet = null;

        try {

            con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

            statement = con.prepareStatement(createSql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, currency);

            statement.executeUpdate();

            resultSet = statement.getGeneratedKeys();

            if (!resultSet.next()) throw new IllegalStateException("Cannot insert new account");

            final long id = resultSet.getLong(1);

            resultSet.close(); resultSet = null;
            statement.close(); statement = null;

            statement = con.prepareStatement(getByIdSql);

            statement.setLong(1, id);

            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                final long readId = resultSet.getLong(1);
                final String readCurrency = resultSet.getString(2);
                final double readAmount = resultSet.getDouble(3);

                account = new Account(readId, readCurrency, new BigDecimal(readAmount));
            } else throw new IllegalStateException("Cannot insert new account");

            con.commit();

        } catch (Throwable th) {
            log.error("getAccount error", th);
            con.rollback();
            throw th;
        }
        finally {

            if(resultSet != null) resultSet.close();
            if(statement != null) statement.close();

            dbConnectionProvider.releaseConnection(con);
        }

        return account;
    }

    @Override
    public @Nullable Account getAccount(@Nonnull Long id) throws SQLException {

        @Nonnull final Connection con = dbConnectionProvider.getConnection();

        @Nullable Account account = null;
        @Nullable PreparedStatement statement = null;
        @Nullable  ResultSet resultSet = null;

        try {

            con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

            statement = con.prepareStatement(getByIdSql);

            statement.setLong(1, id);

            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                final long readId = resultSet.getLong(1);
                final String readCurrency = resultSet.getString(2);
                final double readAmount = resultSet.getDouble(3);

                account = new Account(readId, readCurrency, new BigDecimal(readAmount));
            }

            con.commit();

        } catch (Throwable th) {
            log.error("getAccount error", th);
            con.rollback();
            throw th;
        }
        finally {

            if(resultSet != null) resultSet.close();
            if(statement != null) statement.close();

            dbConnectionProvider.releaseConnection(con);
        }

        return account;
    }
}

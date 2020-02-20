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

@SuppressFBWarnings({"RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE"})
public class AccountServiceImpl extends BaseService implements IAccountService {

    private static final Logger log = LogManager.getLogger(AccountServiceImpl.class);

    private static @Nonnull final String getByIdSql = "SELECT id, currency, amount FROM accounts WHERE id = ? FOR UPDATE";
    private static @Nonnull final String getByIdSqlForUp = getByIdSql + " FOR UPDATE";
    private static @Nonnull final String createSql = "INSERT INTO accounts (currency) VALUES (?)";

    public AccountServiceImpl(@Nonnull DbConnectionProvider dbConnectionProvider) {
        super(dbConnectionProvider);
    }

    @Override
    public @Nonnull Account createAccount(@Nonnull final String currency) throws SQLException {

        @Nonnull final Connection con = dbConnectionProvider.getConnection();

        try {

            con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

            long id;

            try (final PreparedStatement statement = con.prepareStatement(createSql, Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, currency);

                statement.executeUpdate();

                try (final ResultSet resultSet = statement.getGeneratedKeys()) {

                    if (!resultSet.next()) throw new SQLException("Cannot insert new account");

                    id = resultSet.getLong(1);
                }
            }

            @Nullable final Account account = getAccountInternal(con, id, false);

            con.commit();

            if(account == null) throw new SQLException("Cannot insert new account");

            return account;

        } catch (Throwable th) {
            log.error("createAccount error", th);
            con.rollback();
            throw th;
        }
        finally {
            dbConnectionProvider.releaseConnection(con);
        }
    }

    @Override
    public @Nullable Account getAccount(@Nonnull final Long id) throws SQLException {

        @Nonnull final Connection con = dbConnectionProvider.getConnection();

        try {

            con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

            @Nullable final Account account = getAccountInternal(con, id, false);

            con.commit();

            return account;

        } catch (Throwable th) {
            log.error("getAccount error", th);
            con.rollback();
            throw th;
        }
        finally {
            dbConnectionProvider.releaseConnection(con);
        }
    }

    private @Nullable Account getAccountInternal(@Nonnull final Connection con, @Nonnull final Long id, final boolean forUpdate) throws SQLException {

        try (@Nonnull final PreparedStatement statement = con.prepareStatement(forUpdate ? getByIdSqlForUp : getByIdSql)) {

            statement.setLong(1, id);

            try (@Nonnull final ResultSet resultSet = statement.executeQuery()) {

                if(resultSet.next()) {
                    final long readId = resultSet.getLong(1);
                    final String readCurrency = resultSet.getString(2);
                    final double readAmount = resultSet.getDouble(3);

                    return new Account(readId, readCurrency, new BigDecimal(readAmount));
                }
            }
        }

        return null;
    }

}

package com.revolut.dmylnev.services;

import com.revolut.dmylnev.business.exceptions.*;
import com.revolut.dmylnev.database.DbConnectionProvider;
import com.revolut.dmylnev.entity.Account;
import com.revolut.dmylnev.entity.Activity;
import com.revolut.dmylnev.entity.ActivityType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dmylnev
 * @since 19.02.2020
 */

@SuppressFBWarnings({"RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE"})
public class AccountServiceImpl extends BaseService implements IAccountService {

    private static final Logger log = LogManager.getLogger(AccountServiceImpl.class);

    private static final double EPS = 0.01;
    private static final int CUR_MAX_LEN = 10;

    private static @Nonnull final String getByIdSql = "SELECT id, currency, amount, version FROM accounts WHERE id = ?";
    private static @Nonnull final String getByIdSqlForUp = getByIdSql + " FOR UPDATE";
    private static @Nonnull final String createAccountSql = "INSERT INTO accounts (currency) VALUES (?)";
    private static @Nonnull final String updateAccountSql = "UPDATE accounts SET amount = ?, version = ? WHERE id = ?";
    private static @Nonnull final String insertActivitySql = "INSERT INTO activities (type, currency, amount, account, counterpart) VALUES (?, ?, ?, ?, ?)";
    private static @Nonnull final String calcActivitySql = "SELECT sum(amount) FROM activities WHERE account = ?";

    public AccountServiceImpl(@Nonnull DbConnectionProvider dbConnectionProvider) {
        super(dbConnectionProvider);
    }

    @Override
    public @Nonnull Account createAccount(@Nonnull final String currency) throws SQLException {

        validateCurrency(currency);

        @Nonnull final Connection con = dbConnectionProvider.getConnection();

        con.setAutoCommit(false);

        try {

            con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

            long id;

            try (final PreparedStatement statement = con.prepareStatement(createAccountSql, Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, currency);

                statement.executeUpdate();

                try (final ResultSet resultSet = statement.getGeneratedKeys()) {

                    if (!resultSet.next()) throw new SQLException("Cannot insert new account");

                    id = resultSet.getLong(1);
                }
            }

            @Nullable final Account account = getAccountInternal(con, id, false);

            if(account == null) throw new SQLException("Cannot insert new account");

            con.commit();

            return account;

        } catch (Throwable th) {
            log.error("createAccount error", th);
            con.rollback();
            throw th;
        }
        finally {
            con.setAutoCommit(true);
            dbConnectionProvider.releaseConnection(con);
        }
    }

    @Override
    public @Nullable Account getAccount(final long id) throws SQLException {

        @Nonnull final Connection con = dbConnectionProvider.getConnection();

        con.setAutoCommit(false);

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
            con.setAutoCommit(true);
            dbConnectionProvider.releaseConnection(con);
        }
    }

    @Override
    public @Nonnull Activity withdrawal(final long id, @Nonnull final String currency, final double amount) throws SQLException, BusinessException {
        return deposit(id, currency, -amount);
    }

    @Override
    public @Nonnull Activity deposit(final long id, @Nonnull final String currency, final double amount) throws SQLException, BusinessException {

        validateCurrency(currency);

        if(Math.abs(amount) < EPS) throw new SmallAmountException(amount);

        @Nonnull final Connection con = dbConnectionProvider.getConnection();

        con.setAutoCommit(false);

        try {

            con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

            @Nullable final Account account = getAccountInternal(con, id, true);

            if(account == null) throw new AccountNotFoundException(id);
            if(!account.currency.equals(currency)) throw new DifferentCurrenciesException(account.currency, currency);

            if( (amount < 0) && (account.amount < Math.abs(amount)) ) {
                throw new NotEnoughMoneyException(account.id, account.amount, Math.abs(amount));
            }

            final long activityId = insertActivity(con, (amount > 0) ? ActivityType.DEPOSIT : ActivityType.WITHDRAWAL,
                                                   account.id, account.currency, amount, null);


            final double newAmount = account.amount + amount;

            updateAccount(con, account.id, newAmount, activityId);

            con.commit();

            return new Activity(activityId, (amount > 0) ? ActivityType.DEPOSIT : ActivityType.WITHDRAWAL, account.currency, amount, account.id, null);

        } catch (Throwable th) {
            log.error("deposit error", th);
            con.rollback();
            throw th;
        }
        finally {
            con.setAutoCommit(true);
            dbConnectionProvider.releaseConnection(con);
        }
    }

    @Override
    public @Nonnull List<Activity> transfer(final long from, final long to, @Nonnull final String currency, final double amount)
            throws SQLException, BusinessException {

        validateCurrency(currency);

        if(from == to) throw new SameAccountTransferException(from);

        if(amount < 0) throw new IllegalArgumentException("Amount must be positive " + amount);
        if(amount < EPS) throw new SmallAmountException(amount);

        @Nonnull final Connection con = dbConnectionProvider.getConnection();

        con.setAutoCommit(false);

        try {

            con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

            @Nullable Account accountFrom;
            @Nullable Account accountTo;

            if(from < to) { // lock at first account with less id for preventing deadlock !!!
                accountFrom = getAccountInternal(con, from, true);
                accountTo   = getAccountInternal(con, to, true);
            } else {
                accountTo   = getAccountInternal(con, to, true);
                accountFrom = getAccountInternal(con, from, true);
            }

            if(accountFrom == null) throw new AccountNotFoundException(from);
            if(accountTo == null) throw new AccountNotFoundException(to);

            if(!accountFrom.currency.equals(currency)) throw new DifferentCurrenciesException(accountFrom.currency, currency);
            if(!accountTo.currency.equals(currency)) throw new DifferentCurrenciesException(accountTo.currency, currency);

            if(accountFrom.amount < amount) throw new NotEnoughMoneyException(accountFrom.id, accountFrom.amount, Math.abs(amount));

            final long activityFrom = insertActivity(con, ActivityType.TRANSFER_FROM, from, currency, -amount, to);
            final long activityTo = insertActivity(con, ActivityType.TRANSFER_TO, to, currency, amount, from);

            updateAccount(con, from, accountFrom.amount - amount, activityFrom);
            updateAccount(con, to, accountTo.amount + amount, activityTo);

            con.commit();

            @Nonnull final List<Activity> result = new ArrayList<>(2);

            result.add(0, new Activity(activityFrom, ActivityType.TRANSFER_FROM, currency, -amount, from, to));
            result.add(1, new Activity(activityTo, ActivityType.TRANSFER_TO, currency, amount, to, from));

            return result;

        } catch (Throwable th) {
            log.error("transfer error", th);
            con.rollback();
            throw th;
        }
        finally {
            con.setAutoCommit(true);
            dbConnectionProvider.releaseConnection(con);
        }
    }

    @Override
    public double recalculateBalanceByActivities(final long id) throws SQLException, BusinessException {

        @Nonnull final Connection con = dbConnectionProvider.getConnection();

        con.setAutoCommit(false);

        try {

            con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

            @Nullable final Account account = getAccountInternal(con, id, true);

            if(account == null) throw new AccountNotFoundException(id);

            double balance = 0;

            try (@Nonnull final PreparedStatement statement = con.prepareStatement(calcActivitySql)) {

                statement.setLong(1, id);

                try (@Nonnull final ResultSet resultSet = statement.executeQuery()) {

                    if(resultSet.next()) {
                        balance = resultSet.getDouble(1);
                    }
                }
            }

            con.commit();

            return balance;

        } catch (Throwable th) {
            log.error("recalculateBalanceByActivities error", th);
            con.rollback();
            throw th;
        }
        finally {
            con.setAutoCommit(true);
            dbConnectionProvider.releaseConnection(con);
        }
    }

    private @Nullable Account getAccountInternal(@Nonnull final Connection con, @Nonnull final Long id, final boolean forUpdate) throws SQLException {

        try (@Nonnull final PreparedStatement statement = con.prepareStatement(forUpdate ? getByIdSqlForUp : getByIdSql)) {

            statement.setLong(1, id);

            try (@Nonnull final ResultSet resultSet = statement.executeQuery()) {

                if(resultSet.next()) {

                    return new Account(resultSet.getLong(1),
                                       resultSet.getString(2),
                                       resultSet.getDouble(3),
                                       resultSet.getLong(4));
                }
            }
        }

        return null;
    }

    private long insertActivity(@Nonnull final Connection con,
                                @Nonnull final ActivityType type,
                                final long id,
                                @Nonnull final String currency,
                                final double amount,
                                @Nullable final Long target) throws SQLException {

        validateCurrency(currency);

        try (final PreparedStatement statement = con.prepareStatement(insertActivitySql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, type.name());
            statement.setString(2, currency);
            statement.setDouble(3, amount);
            statement.setLong(4, id);

            if(target == null) statement.setNull(5, Types.BIGINT);
            else               statement.setLong(5, target);


            statement.executeUpdate();

            try (final ResultSet resultSet = statement.getGeneratedKeys()) {

                if (!resultSet.next()) throw new SQLException("Cannot insert activity");

                return resultSet.getLong(1);
            }
        }
    }

    private void updateAccount(@Nonnull final Connection con, final long id, final double amount, final long version) throws SQLException {

        try (final PreparedStatement statement = con.prepareStatement(updateAccountSql)) {

            statement.setDouble(1, amount);
            statement.setLong(2, version);
            statement.setLong(3, id);

            statement.execute();
        }
    }

    private void validateCurrency(@Nullable final String currency) {

        if( (currency == null) || (currency.isBlank())) throw new IllegalArgumentException("Empty currency name {" + currency + "}");

        if(currency.length() > CUR_MAX_LEN) throw new IllegalArgumentException("Too long currency name {" + currency + "}");
    }

}

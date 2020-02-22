package com.revolut.dmylnev.entity;

import com.google.gson.Gson;
import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author dmylnev
 * @since 19.02.2020
 */

public final class Account {

    public static @Nonnull final String PARAM_CURRENCY = "currency";
    public static @Nonnull final String PARAM_AMOUNT = "amount";
    public static @Nonnull final String PARAM_FROM = "from";
    public static @Nonnull final String PARAM_TO = "to";

    public final long id;
    public @Nonnull final String currency;
    public final double amount;
    public final long version;

    public Account(final long id, @Nonnull final String currency, final double amount, final long version) {
        this.id = id;
        this.currency = Objects.requireNonNull(currency);
        this.amount = amount;
        this.version = version;
    }

    @Override
    public String toString() {
        return toJson();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Account account = (Account) o;

        return id == account.id &&
               amount == account.amount &&
               version == account.version &&
               currency.equals(account.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currency, amount);
    }

    public @Nonnull String toJson() {
        return new Gson().toJson(this, Account.class);
    }

    public static @Nonnull Account fromJson(@Nonnull final String json) {

        Objects.requireNonNull(json);

        @Nonnull final Account account = new Gson().fromJson(json, Account.class);

        if( (account.id == 0) || (account.currency == null) ) throw new IllegalArgumentException(json);

        return account;
    }
}

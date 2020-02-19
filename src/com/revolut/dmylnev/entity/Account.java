package com.revolut.dmylnev.entity;

import com.google.gson.Gson;
import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author dmylnev
 * @since 19.02.2020
 */

public class Account {

    public static @Nonnull final String PARAM_CURRENCY = "currency";

    public @Nonnull final Long id;
    public @Nonnull final String currency;
    public @Nonnull final BigDecimal amount;

    public Account(@Nonnull final Long id, @Nonnull final String currency, @Nonnull final BigDecimal amount) {
        this.id = Objects.requireNonNull(id);
        this.currency = Objects.requireNonNull(currency);
        this.amount = Objects.requireNonNull(amount);
    }

    public @Nonnull String toJson() {
        return new Gson().toJson(this, Account.class);
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

        return id.equals(account.id) &&
               currency.equals(account.currency) &&
               amount.compareTo(account.amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currency, amount);
    }

    public static @Nonnull Account fromJson(@Nonnull final String json) {

        Objects.requireNonNull(json);

        @Nonnull final Account account = new Gson().fromJson(json, Account.class);

        if( (account.id == null) || (account.currency == null) || (account.amount == null) ) throw new IllegalArgumentException(json);

        return account;
    }
}

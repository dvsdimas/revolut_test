package com.revolut.dmylnev.entity;

import com.google.gson.Gson;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author dmylnev
 * @since 20.02.2020
 */

public class Activity {

    public final long id;
    public @Nonnull final ActivityType type;
    public @Nonnull final String currency;
    public final double amount;
    public final long account;
    public @Nullable final Long target;

    public Activity(final long id,
                    @Nonnull final ActivityType type,
                    @Nonnull final String currency,
                    final double amount,
                    final long account,
                    @Nullable final Long target) {

        this.id = id;
        this.type = Objects.requireNonNull(type);
        this.currency = Objects.requireNonNull(currency);
        this.amount = amount;
        this.account = account;
        this.target = target;
    }

    public @Nonnull String toJson() {
        return new Gson().toJson(this, Activity.class);
    }

    public static @Nonnull Activity fromJson(@Nonnull final String json) {

        Objects.requireNonNull(json);

        @Nonnull final Activity activity = new Gson().fromJson(json, Activity.class);

        if( (activity.id == 0) || (activity.currency == null) || (activity.type == null) || (activity.account == 0) )
            throw new IllegalArgumentException(json);

        return activity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Activity activity = (Activity) o;

        return id == activity.id &&
                account == activity.account &&
                type == activity.type &&
                Double.compare(activity.amount, amount) == 0 &&
                Objects.equals(target, activity.target) &&
                currency.equals(activity.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, currency, amount, account, target);
    }
}

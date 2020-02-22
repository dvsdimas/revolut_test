package com.revolut.dmylnev.business.exceptions;

import com.google.gson.Gson;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dmylnev
 * @since 22.02.2020
 */

public final class AccountNotFoundException extends BusinessException {

    public static @Nonnull final String msg = "Account with specified id has not been found";

    private final long accountId;

    public AccountNotFoundException(final long accountId) {
        this.accountId = accountId;
    }

    @Override
    public @Nonnull String toJson() {

        @Nonnull final Map<String, String> map = new HashMap<>();

        map.put(PARAM_REASON, msg);
        map.put(PARAM_ACCOUNT_ID, String.valueOf(accountId));

        return new Gson().toJson(map);
    }

    public static @Nonnull AccountNotFoundException fromJson(@Nonnull String json) {

        @Nonnull final Map<String, String> map = new Gson().fromJson(json, Map.class);

        return new AccountNotFoundException(Long.parseLong(map.get(PARAM_ACCOUNT_ID)));
    }

    @Override
    public String toString() {
        return toJson();
    }
}

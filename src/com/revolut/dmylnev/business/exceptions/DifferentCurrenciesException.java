package com.revolut.dmylnev.business.exceptions;

import com.google.gson.Gson;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dmylnev
 * @since 22.02.2020
 */

public class DifferentCurrenciesException extends BusinessException {

    public static @Nonnull final String msg = "Account has different currency than in requested operation";

    public @Nonnull final String accountCurrency;
    public @Nonnull final String operationCurrency;

    public DifferentCurrenciesException(@Nonnull final String accountCurrency, @Nonnull final String operationCurrency) {
        this.accountCurrency = accountCurrency;
        this.operationCurrency = operationCurrency;
    }

    @Override
    public @Nonnull String toJson() {

        @Nonnull final Map<String, String> map = new HashMap<>();

        map.put(PARAM_REASON, msg);
        map.put(PARAM_ACCOUNT_CURRENCY, accountCurrency);
        map.put(PARAM_OPERATION_CURRENCY, operationCurrency);

        return new Gson().toJson(map);
    }

    public static @Nonnull DifferentCurrenciesException fromJson(@Nonnull String json) {

        @Nonnull final Map<String, String> map = new Gson().fromJson(json, Map.class);

        return new DifferentCurrenciesException(map.get(PARAM_ACCOUNT_CURRENCY), map.get(PARAM_OPERATION_CURRENCY));
    }

    @Override
    public String toString() {
        return toJson();
    }

}

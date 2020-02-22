package com.revolut.dmylnev.business.exceptions;

import com.google.gson.Gson;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dmylnev
 * @since 22.02.2020
 */

public class SmallAmountException extends BusinessException {

    public static @Nonnull final String msg = "Operation amount cannot be less than 1 cent";

    private final double operationAmount;

    public SmallAmountException(final double operationAmount) {
        this.operationAmount = operationAmount;
    }

    @Override
    public @Nonnull String toJson() {

        @Nonnull final Map<String, String> map = new HashMap<>();

        map.put(PARAM_REASON, msg);
        map.put(PARAM_OPERATION_AMOUNT, String.valueOf(operationAmount));

        return new Gson().toJson(map);
    }

    public static @Nonnull SmallAmountException fromJson(@Nonnull String json) {

        @Nonnull final Map<String, String> map = new Gson().fromJson(json, Map.class);

        return new SmallAmountException(Double.parseDouble(map.get(PARAM_OPERATION_AMOUNT)));
    }

    @Override
    public String toString() {
        return toJson();
    }

}

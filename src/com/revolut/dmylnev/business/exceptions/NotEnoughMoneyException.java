package com.revolut.dmylnev.business.exceptions;

import com.google.gson.Gson;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dmylnev
 * @since 21.02.2020
 */

public final class NotEnoughMoneyException extends BusinessException {

    public static @Nonnull final String msg = "Not enough money on the account for the operation";

    private final long accountId;
    private final double accountAmount;
    private final double requestedAmount;

    public NotEnoughMoneyException(final long accountId, final double accountAmount, final double requestedAmount) {
        this.accountId = accountId;
        this.accountAmount = accountAmount;
        this.requestedAmount = requestedAmount;
    }

    @Override
    public @Nonnull String toJson() {

        @Nonnull final Map<String, String> map = new HashMap<>();

        map.put(PARAM_REASON, msg);
        map.put(PARAM_ACCOUNT_ID, String.valueOf(accountId));
        map.put(PARAM_ACCOUNT_AMOUNT, String.valueOf(accountAmount));
        map.put(PARAM_REQUESTED_AMOUNT, String.valueOf(requestedAmount));

        return new Gson().toJson(map);
    }

    public static @Nonnull NotEnoughMoneyException fromJson(@Nonnull String json) {

        @Nonnull final Map<String, String> map = new Gson().fromJson(json, Map.class);

        return new NotEnoughMoneyException(Long.parseLong(map.get(PARAM_ACCOUNT_ID)),
                                           Double.parseDouble(map.get(PARAM_ACCOUNT_AMOUNT)),
                                           Double.parseDouble(map.get(PARAM_REQUESTED_AMOUNT))
        );
    }
}

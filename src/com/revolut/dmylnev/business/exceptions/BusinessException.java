package com.revolut.dmylnev.business.exceptions;

import javax.annotation.Nonnull;

/**
 * @author dmylnev
 * @since 21.02.2020
 */

public abstract class BusinessException extends Exception {

    public static @Nonnull final String PARAM_REASON = "reason";
    public static @Nonnull final String PARAM_ACCOUNT_ID = "accountId";
    public static @Nonnull final String PARAM_ACCOUNT_AMOUNT = "accountAmount";
    public static @Nonnull final String PARAM_REQUESTED_AMOUNT = "requestedAmount";
    public static @Nonnull final String PARAM_ACCOUNT_CURRENCY = "accountCurrency";
    public static @Nonnull final String PARAM_OPERATION_CURRENCY = "operationCurrency";

    public abstract @Nonnull String toJson();
}

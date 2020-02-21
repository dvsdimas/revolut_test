package com.revolut.dmylnev.services;

import com.revolut.dmylnev.business.exceptions.BusinessException;
import com.revolut.dmylnev.entity.Account;
import com.revolut.dmylnev.entity.Activity;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.SQLException;

/**
 * @author dmylnev
 * @since 19.02.2020
 */

public interface IAccountService {

    @Nonnull Account createAccount(@Nonnull final String currency) throws SQLException;

    @Nullable Account getAccount(@Nonnull final Long id) throws SQLException;

    @Nonnull Activity deposit(final long id, @Nonnull final String currency, final double amount) throws SQLException, BusinessException;

}

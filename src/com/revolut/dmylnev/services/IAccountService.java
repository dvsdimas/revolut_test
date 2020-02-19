package com.revolut.dmylnev.services;

import com.revolut.dmylnev.entity.Account;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.SQLException;

/**
 * @author dmylnev
 * @since 19.02.2020
 */

public interface IAccountService {

    @Nonnull Account createAccount(@Nonnull final String currency);

    @Nullable Account getAccount(@Nonnull final Long id) throws SQLException;

}

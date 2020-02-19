package com.revolut.dmylnev.services;

import com.revolut.dmylnev.entity.Account;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author dmylnev
 * @since 19.02.2020
 */

public interface IAccountService {

    @Nonnull Account createAccount(@Nonnull final String currency, @Nonnull final UUID uuid);

    @Nullable Account getAccount(@Nonnull final Long id) throws SQLException;

}

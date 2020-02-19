package com.revolut.dmylnev.rest.jetty.servlets;

import com.revolut.dmylnev.entity.Account;
import com.revolut.dmylnev.services.ServicesProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author dmylnev
 * @since 19.02.2020
 */

public class AccountServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(AccountServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {

            @Nonnull final String strId = req.getPathInfo().substring(1);

            log.info("Trying to get Account with the id [{}]", strId);

            @Nonnull final Long id = Long.valueOf(strId);

            @Nullable final Account account = ServicesProvider.getAccountService().getAccount(id);

            if(account == null) {

                @Nonnull final String msg = String.format("Account with the id [%d] not found", id);

                log.warn(msg);

                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().print(msg);

            } else {

                @Nonnull final String json = account.toJson();

                log.info("Found account [{}]", json);

                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().print(json);
            }
        } catch (Throwable th) {

            log.error("get account by id error", th);

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(th.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        final String[] ca = req.getParameterValues(Account.PARAM_CURRENCY);

        if( (ca == null) || ca[0] == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Param " + Account.PARAM_CURRENCY + " not found !");
            return;
        }

        final String currency = ca[0];

        log.info("Creating account with currency [{}]", currency);

        @Nonnull final Account account = ServicesProvider.getAccountService().createAccount(currency);

        @Nonnull final String json = account.toJson();

        log.info("Created new account [{}]", json);

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().print(json);
    }

}

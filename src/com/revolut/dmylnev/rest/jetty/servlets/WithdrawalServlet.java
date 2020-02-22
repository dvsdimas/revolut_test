package com.revolut.dmylnev.rest.jetty.servlets;

import com.revolut.dmylnev.business.exceptions.BusinessException;
import com.revolut.dmylnev.entity.Account;
import com.revolut.dmylnev.entity.Activity;
import com.revolut.dmylnev.services.ServicesProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author dmylnev
 * @since 20.02.2020
 */

public class WithdrawalServlet extends BusinessServlet {

    private static final Logger log = LogManager.getLogger(WithdrawalServlet.class);

    @Override
    protected void doPostInternal(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException, BusinessException {

        final long id = Long.parseLong(req.getPathInfo().substring(1));


        final String[] ca = req.getParameterValues(Account.PARAM_CURRENCY);

        if ((ca == null) || ca[0] == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Param " + Account.PARAM_CURRENCY + " not found !");
            return;
        }

        final String currency = ca[0];

        final String[] aa = req.getParameterValues(Account.PARAM_AMOUNT);

        if ((aa == null) || aa[0] == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Param " + Account.PARAM_AMOUNT + " not found !");
            return;
        }

        final double amount = Double.parseDouble(aa[0]);

        log.info("Trying to withdrawal from Account with the id [{}] {} {}", id, amount, currency);

        if (amount < 0) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("amount must be more than zero!");
            return;
        }

        @Nonnull final Activity activity = ServicesProvider.getAccountService().withdrawal(id, currency, amount);

        @Nonnull final String json = activity.toJson();

        log.info("Withdrawal has been made [{}]", json);

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().print(json);
    }

    @Override
    protected void doGetInternal(HttpServletRequest req, HttpServletResponse resp) {
        throw new IllegalStateException("doGetInternal has not been implemented");
    }
}

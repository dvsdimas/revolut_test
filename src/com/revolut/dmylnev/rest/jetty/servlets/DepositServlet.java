package com.revolut.dmylnev.rest.jetty.servlets;

import com.revolut.dmylnev.entity.Account;
import com.revolut.dmylnev.entity.Activity;
import com.revolut.dmylnev.services.ServicesProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.Nonnull;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author dmylnev
 * @since 20.02.2020
 */

public class DepositServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(DepositServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {

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

            log.info("Trying to deposit to Account with the id [{}] {} {}", id, amount, currency);

            if (amount < 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("amount must be more than zero!");
                return;
            }

            if (amount < 0.01) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Deposit must be more than 1 cent!");
                return;
            }

            @Nonnull final Activity activity = ServicesProvider.getAccountService().deposit(id, currency, amount);

            @Nonnull final String json = activity.toJson();

            log.info("Deposit has been made [{}]", json);

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().print(json);

        }
        catch (Throwable th) {

            log.error("deposit error", th);

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(th.getMessage());
        }

    }

}

package com.revolut.dmylnev.rest.jetty.servlets;

import com.google.gson.Gson;
import com.revolut.dmylnev.business.exceptions.BusinessException;
import com.revolut.dmylnev.entity.Account;
import com.revolut.dmylnev.entity.Activity;
import com.revolut.dmylnev.services.ServicesProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dmylnev
 * @since 22.02.2020
 */

public class TransferServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(TransferServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {

            @Nullable final String[] fa = req.getParameterValues(Account.PARAM_FROM);

            if ((fa == null) || fa[0] == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Param " + Account.PARAM_FROM + " not found !");
                return;
            }

            final long from = Long.parseLong(fa[0]);

            //----------------------------------------------------------------------------------------------------------

            @Nullable final String[] ta = req.getParameterValues(Account.PARAM_TO);

            if ((ta == null) || ta[0] == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Param " + Account.PARAM_TO + " not found !");
                return;
            }

            final long to = Long.parseLong(ta[0]);

            //----------------------------------------------------------------------------------------------------------

            final String[] ca = req.getParameterValues(Account.PARAM_CURRENCY);

            if ((ca == null) || ca[0] == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Param " + Account.PARAM_CURRENCY + " not found !");
                return;
            }

            final String currency = ca[0];

            //----------------------------------------------------------------------------------------------------------

            final String[] aa = req.getParameterValues(Account.PARAM_AMOUNT);

            if ((aa == null) || aa[0] == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Param " + Account.PARAM_AMOUNT + " not found !");
                return;
            }

            final double amount = Double.parseDouble(aa[0]);

            //----------------------------------------------------------------------------------------------------------

            log.info("Trying to transfer from Account {} to Account {} {} {}", from, to, amount, currency);

            if (amount < 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("amount must be more than zero!");
                return;
            }

            @Nonnull final List<Activity> result = ServicesProvider.getAccountService().transfer(from, to, currency, amount);

            @Nonnull final Map<String, String> map = new HashMap<>();

            @Nonnull final String fromJson = result.get(0).toJson();
            @Nonnull final String toJson = result.get(1).toJson();

            map.put("from", fromJson);
            map.put("to", toJson);

            @Nonnull final String out = new Gson().toJson(map);

            log.info("Transfer has been made [{}]", out);

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().print(out);

        }
        catch (BusinessException bex) {

            log.warn("transfer business error", bex);

            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().print(bex.toJson());
        }
        catch (Throwable th) {

            log.error("transfer error", th);

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(th.getMessage());
        }

    }

}

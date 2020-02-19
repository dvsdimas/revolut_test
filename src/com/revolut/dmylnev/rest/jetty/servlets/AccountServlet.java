package com.revolut.dmylnev.rest.jetty.servlets;

import com.revolut.dmylnev.entity.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author dmylnev
 * @since 19.02.2020
 */

public class AccountServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(AccountServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {



//        req.getPathInfo()

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println("{ \"status\": \"ok\"}");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        final String[] ca = req.getParameterValues(Account.PARAM_CURRENCY);
        final String[] ua = req.getParameterValues(Account.PARAM_UUID);

        if( (ca == null) || ca[0] == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Param " + Account.PARAM_CURRENCY + " not found !");
            return;
        }

        if( (ua == null) || ua[0] == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Param " + Account.PARAM_UUID + " not found !");
            return;
        }

        final String currency = ca[0];
        final UUID uuid = UUID.fromString(ua[0]);

        log.info("Creating account with currency [{}] and uuid [{}]", currency, uuid.toString());


    }

}

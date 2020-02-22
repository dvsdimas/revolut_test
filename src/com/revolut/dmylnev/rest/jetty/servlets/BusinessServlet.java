package com.revolut.dmylnev.rest.jetty.servlets;

import com.revolut.dmylnev.business.exceptions.BusinessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author dmylnev
 * @since 22.02.2020
 */

public abstract class BusinessServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger(BusinessServlet.class);

    protected abstract void doPostInternal(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, SQLException, BusinessException;

    protected abstract void doGetInternal(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException;

    @Override
    protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            doPostInternal(req, resp);
        }
        catch (BusinessException bex) {

            log.warn("business error {}", bex.toJson());

            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().print(bex.toJson());
        }
        catch (Throwable th) {

            log.error("server error", th);

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(th.getMessage());
        }
    }

    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            doGetInternal(req, resp);
        } catch (Throwable th) {

            log.error("server error", th);

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().print(th.getMessage());
        }
    }

}

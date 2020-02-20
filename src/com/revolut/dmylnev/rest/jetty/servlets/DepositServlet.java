package com.revolut.dmylnev.rest.jetty.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {



    }

}

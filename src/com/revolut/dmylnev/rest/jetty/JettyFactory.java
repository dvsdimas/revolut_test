package com.revolut.dmylnev.rest.jetty;

import com.revolut.dmylnev.rest.jetty.servlets.AccountServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import javax.annotation.Nonnull;

/**
 * @author dmylnev
 * @since 18.02.2020
 */

public final class JettyFactory {

    private JettyFactory() { throw new IllegalStateException(); }

    public static @Nonnull Server createJetty(final int port) {

        @Nonnull final Server server = new Server();

        @Nonnull final ServerConnector connector = new ServerConnector(server);

        connector.setPort(port);

        server.setConnectors(new Connector[] {connector});

        @Nonnull final ServletContextHandler context = new ServletContextHandler();

        context.setContextPath("/");
        context.addServlet(AccountServlet.class, "/account/*");

        @Nonnull final HandlerCollection handlers = new HandlerCollection();

        handlers.setHandlers(new Handler[]{context, new DefaultHandler()});

        server.setHandler(handlers);

        return server;
    }

}
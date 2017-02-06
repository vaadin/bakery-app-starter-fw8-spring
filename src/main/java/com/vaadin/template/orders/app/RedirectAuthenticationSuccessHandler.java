package com.vaadin.template.orders.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Redirects to the application after successful authentication.
 */
public class RedirectAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    private final String location;

    public RedirectAuthenticationSuccessHandler(String location) {
        this.location = location;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        response.sendRedirect(location);

    }

}

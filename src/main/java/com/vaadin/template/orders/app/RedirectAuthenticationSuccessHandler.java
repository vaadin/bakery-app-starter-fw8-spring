package com.vaadin.template.orders.app;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.context.annotation.ApplicationScope;

import com.vaadin.spring.annotation.SpringComponent;

/**
 * Redirects to the application after successful authentication.
 */
@SpringComponent
@ApplicationScope
public class RedirectAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    private final String location;

    @Autowired
    private ServletContext servletContext;

    public RedirectAuthenticationSuccessHandler() {
        location = Application.APP_URL;
    }

    private String getAbsoluteUrl(String url) {
        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        return servletContext.getContextPath() + "/" + url;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        response.sendRedirect(getAbsoluteUrl(location));

    }

}

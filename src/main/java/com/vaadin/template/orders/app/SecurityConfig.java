package com.vaadin.template.orders.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.template.orders.backend.data.Role;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedirectAuthenticationSuccessHandler successHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        super.configure(auth);
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Not using Spring CSRF here to be able to use plain HTML for the login
        // page
        http.csrf().disable();

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry reg = http
                .authorizeRequests();

        // Allow access to static resources ("/VAADIN/**")
        reg = reg.antMatchers("/VAADIN/**").permitAll();
        // Require authentication for all URLS ("/**")
        reg = reg.antMatchers("/**").hasAnyAuthority(Role.getRoleNames());
        HttpSecurity sec = reg.and();

        // Allow access to login page without login
        FormLoginConfigurer<HttpSecurity> login = sec.formLogin().permitAll();
        login = login.loginPage(Application.LOGIN_URL)
                .loginProcessingUrl(Application.LOGIN_PROCESSING_URL)
                .failureUrl(Application.LOGIN_FAILURE_URL)
                .successHandler(successHandler);
        login.and().logout().logoutSuccessUrl(Application.LOGOUT_URL);
    }

}

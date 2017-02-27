package com.vaadin.template.orders.app;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHolder {

    @Autowired
    private ApplicationContext appContext;

    private static SpringContextHolder instance;

    @PostConstruct
    protected void initialize() {
        instance = this;
    }

    public static ApplicationContext getAppContext() {
        return instance.appContext;
    }
}

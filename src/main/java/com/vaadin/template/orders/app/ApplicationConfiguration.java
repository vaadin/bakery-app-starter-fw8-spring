package com.vaadin.template.orders.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vaadin.spring.annotation.PrototypeScope;

import com.vaadin.data.converter.StringToIntegerConverter;

@Configuration
public class ApplicationConfiguration {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public StringToIntegerConverter stringToIntegerConverter() {
		return new StringToIntegerConverter("Unable to parse integer");
	}

	@Bean
	@PrototypeScope
	public Logger configureLogger(InjectionPoint injectionPoint) {
		Class<?> injectionPointType = injectionPoint.getMember().getDeclaringClass();
		return LoggerFactory.getLogger(injectionPointType);
	}
}

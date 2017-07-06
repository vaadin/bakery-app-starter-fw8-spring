package com.vaadin.starter.bakery.backend.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.core.env.Environment;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinServletService;
import com.vaadin.starter.bakery.app.BeanLocator;

public class BlacklistValidator implements ConstraintValidator<Blacklist, String> {

	String key;
	String[] list;

	@Override
	public void initialize(Blacklist constraintAnnotation) {
		this.key = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
		if (object == null || key == null) {
			return true;
		}

		if (list == null) {
			Environment env = BeanLocator.find(Environment.class);
			list = env.getProperty(key, String[].class);
		}

		if (list != null) {
			for (String word : list) {
				if (object.matches(word)) {
					String ip = Page.getCurrent().getWebBrowser().getAddress();
					String xff = VaadinServletService.getCurrentServletRequest().getHeader("x-forwarded-for");
					Logger.getLogger(BlacklistValidator.class.getName()).log(Level.WARNING,
							"Blacklisted word detected from " + ip + " / " + xff);
					return false;
				}
			}
		}
		return true;
	}
}
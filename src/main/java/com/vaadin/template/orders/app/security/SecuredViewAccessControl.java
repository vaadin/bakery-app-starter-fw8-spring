package com.vaadin.template.orders.app.security;

import java.io.Serializable;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.context.WebApplicationContext;

import com.vaadin.navigator.View;
import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.server.SpringVaadinServletService;
import com.vaadin.ui.UI;

@SpringComponent
public class SecuredViewAccessControl implements ViewAccessControl, Serializable {

	private transient WebApplicationContext webApplicationContext = null;

	@Override
	public boolean isAccessGranted(UI ui, String beanName) {
		final Secured viewSecured = getWebApplicationContext(ui).findAnnotationOnBean(beanName, Secured.class);
		return isAccessGranted(ui, viewSecured);
	}

	public boolean isAccessGranted(UI ui, Class<? extends View> viewClass) {
		Secured viewSecured = AnnotationUtils.findAnnotation(viewClass, Secured.class);
		return isAccessGranted(ui, viewSecured);
	}

	private boolean isAccessGranted(UI ui, Secured viewSecured) {
		return SecurityUtils.isAccessGranted(ui, viewSecured);
	}

	protected WebApplicationContext getWebApplicationContext(UI ui) {
		if (webApplicationContext == null) {
			webApplicationContext = ((SpringVaadinServletService) ui.getSession().getService())
					.getWebApplicationContext();
		}

		return webApplicationContext;
	}
}

package com.vaadin.template.orders.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.template.orders.ui.view.AccessDeniedView;
import com.vaadin.ui.UI;

@Theme("orderstheme")
@SpringUI
@Viewport("width=device-width, initial-scale=1.0")
public class OrdersUI extends UI {

	@Autowired
	private SpringViewProvider viewProvider;

	@Autowired
	private NavigationManager navigationManager;

	@Autowired
	private MainView mainView;
	private String username;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
		// Store the user for later reference when we might not have a request
		UsernamePasswordAuthenticationToken p = (UsernamePasswordAuthenticationToken) vaadinRequest.getUserPrincipal();
		UserDetails userDetails = (UserDetails) p.getPrincipal();
		username = userDetails.getUsername();
		setContent(mainView);
		mainView.populateMenu();

		navigationManager.navigateToDefaultView();
	}

	public static OrdersUI get() {
		return (OrdersUI) UI.getCurrent();
	}

	public String getUsername() {
		return username;
	}
}

package com.vaadin.template.orders.ui;

import org.springframework.beans.factory.annotation.Autowired;

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

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
		setContent(mainView);
		mainView.populateMenu();

		navigationManager.navigateToDefaultView();
	}

}

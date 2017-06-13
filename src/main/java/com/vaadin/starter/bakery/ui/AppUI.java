package com.vaadin.starter.bakery.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.addon.charts.ChartOptions;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Viewport;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.starter.bakery.ui.navigation.NavigationManager;
import com.vaadin.starter.bakery.ui.view.AccessDeniedView;
import com.vaadin.ui.UI;

@Theme("apptheme")
@SpringUI
@Viewport("width=device-width,initial-scale=1.0,user-scalable=no")
@Title("###Bakery###")
public class AppUI extends UI {

	private final SpringViewProvider viewProvider;

	private final NavigationManager navigationManager;

	private final MainView mainView;

	@Autowired
	public AppUI(SpringViewProvider viewProvider, NavigationManager navigationManager, MainView mainView) {
		this.viewProvider = viewProvider;
		this.navigationManager = navigationManager;
		this.mainView = mainView;

		// Set the theme ("globally") for all Charts
		ChartOptions.get(this).setTheme(new ChartsTheme());
	}

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
		setContent(mainView);

		navigationManager.navigateToDefaultView();
	}

}

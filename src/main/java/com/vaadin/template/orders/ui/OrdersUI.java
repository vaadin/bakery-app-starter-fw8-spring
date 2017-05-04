package com.vaadin.template.orders.ui;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.internal.Conventions;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.template.orders.backend.data.Role;
import com.vaadin.template.orders.ui.view.AccessDeniedView;
import com.vaadin.template.orders.ui.view.dashboard.DashboardView;
import com.vaadin.template.orders.ui.view.orders.OrderEditView;
import com.vaadin.template.orders.ui.view.orders.OrdersListView;
import com.vaadin.ui.UI;

@Theme("orderstheme")
@SpringUI
@Viewport("width=device-width, initial-scale=1.0")
public class OrdersUI extends UI {

	@Autowired
	private SpringViewProvider viewProvider;
	@Autowired
	private MainView mainView;
	private String username;
	private Set<String> userRoles;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
		// Store the user for later reference when we might not have a request
		UsernamePasswordAuthenticationToken p = (UsernamePasswordAuthenticationToken) vaadinRequest.getUserPrincipal();
		UserDetails userDetails = (UserDetails) p.getPrincipal();
		username = userDetails.getUsername();
		userRoles = userDetails.getAuthorities().stream().map(auth -> ((SimpleGrantedAuthority) auth).getAuthority())
				.collect(Collectors.toSet());
		setContent(mainView);
		mainView.populateMenu();

		navigateToDefaultView();
	}

	private void navigateToDefaultView() {
		// If the user wants a speficic view, it's in the URL.
		// Otherwise admin goes to DashboardView and everybody else to
		// OrderListView

		if (!getNavigator().getState().isEmpty()) {
			return;
		}

		if (getUserRoles().contains(Role.ADMIN)) {
			navigateTo(DashboardView.class);
		} else {
			navigateTo(OrdersListView.class);
		}
	}

	/**
	 * Find the view id (URI fragment) used for a given view class.
	 *
	 * @param viewClass
	 *            the view class to find the id for
	 * @return the URI fragment for the view
	 */
	public static String getViewId(Class<? extends View> viewClass) {
		SpringView springView = viewClass.getAnnotation(SpringView.class);
		if (springView == null) {
			throw new IllegalArgumentException("The target class must be a @SpringView");
		}

		return Conventions.deriveMappingForView(viewClass, springView);
	}

	public void navigateTo(Class<? extends View> targetView) {
		String viewId = getViewId(targetView);
		getNavigator().navigateTo(viewId);
	}

	public void navigateTo(Class<OrderEditView> targetView, Object parameter) {
		String viewId = getViewId(targetView);
		getNavigator().navigateTo(viewId + "/" + parameter.toString());
	}

	public static OrdersUI get() {
		return (OrdersUI) UI.getCurrent();
	}

	public String getUsername() {
		return username;
	}

	public Set<String> getUserRoles() {
		return userRoles;
	}
}

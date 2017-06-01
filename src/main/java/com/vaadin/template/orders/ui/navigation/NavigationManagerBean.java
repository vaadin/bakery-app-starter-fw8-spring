package com.vaadin.template.orders.ui.navigation;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.internal.Conventions;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.template.orders.app.security.SecurityUtils;
import com.vaadin.template.orders.backend.data.Role;
import com.vaadin.template.orders.ui.view.NavigableView;
import com.vaadin.template.orders.ui.view.dashboard.DashboardView;
import com.vaadin.template.orders.ui.view.orders.OrdersListView;

@Component
@UIScope
public class NavigationManagerBean extends SpringNavigator implements NavigationManager {

	/**
	 * Adds support for the beforeLeave method in NavigationEvent so views can
	 * show a confirmation dialog before actually performing the navigation.
	 */
	private final class ViewChangeBeforeLeaveSupport implements ViewChangeListener {

		@Override
		public boolean beforeViewChange(ViewChangeEvent e) {
			View oldView = e.getOldView();
			if (!(oldView instanceof NavigableView)) {
				return true;
			}
			String navigationState = getTargetState(e);
			if (viewAlreadyConfirmed) {
				return true;
			} else {
				return ((NavigableView) oldView).beforeLeave(new NavigationEvent(() -> {
					viewAlreadyConfirmed = true;
					navigateTo(navigationState);
					viewAlreadyConfirmed = false;
				}));
			}
		}

		private String getTargetState(ViewChangeEvent e) {
			if (e.getParameters() == null || e.getParameters().isEmpty()) {
				return e.getViewName();
			} else {
				return e.getViewName() + "/" + e.getParameters();
			}
		}
	}

	boolean viewAlreadyConfirmed = false;

	@PostConstruct
	public void init() {
		addViewChangeListener(new ViewChangeBeforeLeaveSupport());
	}

	/**
	 * Find the view id (URI fragment) used for a given view class.
	 *
	 * @param viewClass
	 *            the view class to find the id for
	 * @return the URI fragment for the view
	 */
	@Override
	public String getViewId(Class<? extends View> viewClass) {
		SpringView springView = viewClass.getAnnotation(SpringView.class);
		if (springView == null) {
			throw new IllegalArgumentException("The target class must be a @SpringView");
		}

		return Conventions.deriveMappingForView(viewClass, springView);
	}

	@Override
	public void navigateTo(Class<? extends View> targetView) {
		String viewId = getViewId(targetView);
		navigateTo(viewId);
	}

	@Override
	public void navigateTo(Class<? extends View> targetView, Object parameter) {
		String viewId = getViewId(targetView);
		navigateTo(viewId + "/" + parameter.toString());
	}

	@Override
	public void navigateToDefaultView() {
		// If the user wants a specific view, it's in the URL.
		// Otherwise admin goes to DashboardView and everybody else to
		// OrderListView
		if (!getState().isEmpty()) {
			return;
		}

		navigateTo(SecurityUtils.isCurrentUserInRole(Role.ADMIN) ? DashboardView.class : OrdersListView.class);
	}

	@Override
	public void updateViewParameter(String parameter) {
		String viewName = getViewId(getCurrentView().getClass());
		String parameters;
		if (parameter == null) {
			parameters = "";
		} else {
			parameters = parameter;
		}

		updateNavigationState(new ViewChangeEvent(this, getCurrentView(), getCurrentView(), viewName, parameters));
	}

}
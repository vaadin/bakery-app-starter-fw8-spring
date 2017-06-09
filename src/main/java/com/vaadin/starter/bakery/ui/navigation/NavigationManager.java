package com.vaadin.starter.bakery.ui.navigation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.internal.Conventions;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.starter.bakery.app.security.SecurityUtils;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.ui.view.NavigableView;
import com.vaadin.starter.bakery.ui.view.dashboard.DashboardView;
import com.vaadin.starter.bakery.ui.view.storefront.StorefrontView;

/**
 * Governs view navigation of the app.
 */
@Component
@UIScope
public class NavigationManager extends SpringNavigator {

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
	public String getViewId(Class<? extends View> viewClass) {
		SpringView springView = viewClass.getAnnotation(SpringView.class);
		if (springView == null) {
			throw new IllegalArgumentException("The target class must be a @SpringView");
		}

		return Conventions.deriveMappingForView(viewClass, springView);
	}

	public void navigateTo(Class<? extends View> targetView) {
		String viewId = getViewId(targetView);
		navigateTo(viewId);
	}

	public void navigateTo(Class<? extends View> targetView, Object parameter) {
		String viewId = getViewId(targetView);
		navigateTo(viewId + "/" + parameter.toString());
	}

	public void navigateToDefaultView() {
		// If the user wants a specific view, it's in the URL.
		// Otherwise admin goes to DashboardView and everybody else to
		// OrderListView
		if (!getState().isEmpty()) {
			return;
		}

		navigateTo(SecurityUtils.isCurrentUserInRole(Role.ADMIN) ? DashboardView.class : StorefrontView.class);
	}

	/**
	 * Update the parameter of the the current view without firing any
	 * navigation events.
	 *
	 * @param parameter
	 *            the new parameter to set, never <code>null</code>,
	 *            <code>""</code> to not use any parameter
	 */
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

	public static Map<String, String> parameterStringToMap(String parameterString) {
		Map<String, String> parameterMap = new HashMap<>();
		String[] parameters = parameterString.split("&");
		for (int i = 0; i < parameters.length; i++) {
			String[] keyAndValue = parameters[i].split("=");
			if (keyAndValue.length > 1) {
				parameterMap.put(keyAndValue[0], keyAndValue[1]);
			} else {
				parameterMap.put(keyAndValue[0], "");
			}
		}

		return parameterMap;
	}

	/**
	 * Parses the given parameter String and returns value for given key if it
	 * exists, null otherwise.
	 * 
	 * @param parameter
	 * @param key
	 * @return value for the key or null if key doesn't exist.
	 */
	public static Optional<String> parseParam(String parameter, String key) {
		return Optional.ofNullable(parameterStringToMap(parameter).get(key));
	}

	/**
	 * Parses the given paramter String and returns true if given key exists,
	 * false otherwise.
	 * 
	 * @param parameter
	 * @param key
	 * @return true if key exists, false otherwise.
	 */
	public static boolean paramAvailable(String parameter, String key) {
		return parameterStringToMap(parameter).containsKey(key);
	}
}

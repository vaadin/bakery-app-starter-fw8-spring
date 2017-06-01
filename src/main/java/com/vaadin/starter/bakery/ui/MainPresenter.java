package com.vaadin.starter.bakery.ui;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.server.VaadinServletService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.starter.bakery.app.Application;
import com.vaadin.starter.bakery.app.security.SecuredViewAccessControl;
import com.vaadin.starter.bakery.ui.navigation.NavigationEvent;
import com.vaadin.starter.bakery.ui.navigation.NavigationManager;
import com.vaadin.starter.bakery.ui.view.NavigableView;

@SpringComponent
@UIScope
public class MainPresenter implements Serializable {

	private MainView view;
	private final NavigationManager navigationManager;
	private final SecuredViewAccessControl viewAccessControl;

	public MainPresenter(@Autowired NavigationManager navigationManager,
			@Autowired SecuredViewAccessControl viewAccessControl) {
		this.navigationManager = navigationManager;
		this.viewAccessControl = viewAccessControl;
	}

	void init(MainView view) {
		this.view = view;
	}

	/**
	 * Navigate to the given view class.
	 *
	 * @param viewClass
	 *            the class of the target view, must be annotated using
	 *            {@link SpringView @SpringView}
	 */
	public void navigateTo(Class<? extends View> viewClass) {
		view.getUI().getNavigator().navigateTo(navigationManager.getViewId(viewClass));
	}

	public void logout() {
		Runnable doLogout = () -> {
			view.getUI().getSession().getSession().invalidate();
			String contextPath = ((VaadinServletService) VaadinServletService.getCurrent()).getServlet()
					.getServletContext().getContextPath();
			view.getUI().getPage().setLocation(contextPath + Application.LOGOUT_URL);
		};
		View currentView = view.getUI().getNavigator().getCurrentView();
		if (currentView instanceof NavigableView) {
			if (((NavigableView) currentView).beforeLeave(new NavigationEvent(doLogout))) {
				doLogout.run();
			}
		} else {
			doLogout.run();
		}
	}

	public boolean hasAccess(Class<? extends View> viewClass) {
		return viewAccessControl.isAccessGranted(view.getUI(), viewClass);
	}
}

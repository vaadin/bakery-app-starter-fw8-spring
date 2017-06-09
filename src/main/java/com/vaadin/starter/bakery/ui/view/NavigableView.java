package com.vaadin.starter.bakery.ui.view;

import java.util.Objects;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A view which is connected to a URL and can be navigated to by the user.
 * <p>
 * Note that this {@code view} is not related to a {@code view} in MVP/MVC or
 * other patterns.
 */
public interface NavigableView extends View {

	/**
	 * Gets the component to show when navigating to the view.
	 *
	 * By default casts this View to a {@link Component}.
	 *
	 * @return the component to show, by default the view instance itself
	 */
	public default Component getViewComponent() {
		return (Component) this;
	}

	/**
	 * Called before leaving a view.
	 * <p>
	 * To proceed with navigation, the {@link NavigationEvent#navigate()} method
	 * must be called, either immediately or later. The view can prevent
	 * navigation away by doing nothing in this method.
	 *
	 * @param event
	 *            the navigation event object
	 * @return <code>true</code> if navigation should take place,
	 *         <code>false</code> to prevent navigation (can be invoked later
	 *         using {@link NavigationEvent#navigate()})
	 */
	public default boolean beforeLeave(Runnable event) {
		return true;
	}

	/**
	 * Shows the standard before leave confirm dialog on given ui. If user
	 * approves the navigation the given runOnConfirm will be executed.
	 *
	 * @param ui
	 * @param runOnConfirm
	 */
	default void showLeaveViewConfirmDialog(Runnable runOnConfirm) {
		showLeaveViewConfirmDialog(runOnConfirm, () -> {
		});
	}

	/**
	 * Shows the standard before leave confirm dialog on given ui. If user
	 * approves the navigation the given runOnConfirm will be executed.
	 *
	 * @param ui
	 * @param runOnConfirm
	 * @param onCancel
	 */
	default void showLeaveViewConfirmDialog(Runnable runOnConfirm, Runnable runOnCancel) {
		ConfirmDialog confirmDialog = ConfirmDialog.show(UI.getCurrent(), "Please confirm",
				"You have unsaved changes that will be discarded if you navigate away.", "Discard Changes", "Cancel",
				Objects.requireNonNull(runOnConfirm));
		confirmDialog.getOkButton().addStyleName(ValoTheme.BUTTON_DANGER);
		if (confirmDialog.isCanceled()) {
			runOnCancel.run();
		}
	}
}

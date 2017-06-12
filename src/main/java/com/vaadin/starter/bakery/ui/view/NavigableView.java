package com.vaadin.starter.bakery.ui.view;

import java.util.Arrays;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.dialogs.DefaultConfirmDialogFactory;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A view which is connected to a URL and can be navigated to by the user.
 * <p>
 * Note that this {@code view} is not related to a {@code view} in MVP/MVC or
 * other patterns.
 */
public interface NavigableView extends View {

	static final DefaultConfirmDialogFactory confirmDialogFactory = new DefaultConfirmDialogFactory() {
		@Override
		protected List<Button> orderButtons(Button cancel, Button notOk, Button ok) {
			return Arrays.asList(ok, cancel);
		}

		@Override
		protected Button buildOkButton(String okCaption) {
			Button okButton = super.buildOkButton(okCaption);
			okButton.addStyleName(ValoTheme.BUTTON_DANGER);
			return okButton;
		}
	};

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
			// Nothing to do on cancel
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
		ConfirmDialog dialog = confirmDialogFactory.create("Please confirm",
				"You have unsaved changes that will be discarded if you navigate away.", "Discard Changes", "Cancel",
				null);
		dialog.show(getViewComponent().getUI(), event -> {
			if (event.isConfirmed()) {
				runOnConfirm.run();
			} else {
				runOnCancel.run();
			}
		}, true);
	}
}

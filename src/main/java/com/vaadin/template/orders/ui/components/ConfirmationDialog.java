package com.vaadin.template.orders.ui.components;

import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class ConfirmationDialog {

	private ConfirmationDialog() {
		// Use show()
	}

	/**
	 * Show a confirmation dialog with the default message and run the given
	 * command if the user clicks the {@literal discard} button.
	 * <p>
	 * Does nothing but closes the dialog if the user clicks the
	 * {@literal cancel} button.
	 *
	 * @param ui
	 *            the ui to show the dialog in
	 * @param onDiscard
	 *            the callback to invoke if the user pushes discard
	 */
	public static void show(UI ui, Runnable onDiscard) {
		show(ui, onDiscard, () -> {
		});
	}

	/**
	 * Show a confirmation dialog with the default message and run the given
	 * command if the user clicks the {@literal discard} button.
	 * <p>
	 * Does nothing but closes the dialog if the user clicks the
	 * {@literal cancel} button.
	 *
	 * @param ui
	 *            the ui to show the dialog in
	 * @param onDiscard
	 *            the callback to invoke if the user pushes discard
	 * @param onCancel
	 *            the callback to invoke if the user pushes cancel
	 */
	public static void show(UI ui, Runnable onDiscard, Runnable onCancel) {
		Window window = new Window("Please confirm");
		window.setModal(true);
		ConfirmationDialogDesign content = new ConfirmationDialogDesign();
		window.setContent(content);

		content.discardChanges.addClickListener(e -> {
			window.close();
			onDiscard.run();
		});
		content.cancel.addClickListener(e -> {
			onCancel.run();
			window.close();
		});
		window.setClosable(false);
		ui.addWindow(window);
	}
}

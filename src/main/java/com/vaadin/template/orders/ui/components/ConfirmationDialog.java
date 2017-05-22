package com.vaadin.template.orders.ui.components;

import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class ConfirmationDialog {

	private ConfirmationDialog() {
		// Use show()
	}

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

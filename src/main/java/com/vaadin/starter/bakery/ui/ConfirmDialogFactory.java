package com.vaadin.starter.bakery.ui;

import java.util.Arrays;
import java.util.List;

import org.vaadin.dialogs.DefaultConfirmDialogFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;

public class ConfirmDialogFactory extends DefaultConfirmDialogFactory {

	@Override
	protected Button buildOkButton(String okCaption) {
		Button button = super.buildOkButton(okCaption);
		button.setStyleName(ValoTheme.BUTTON_DANGER);
		return button;
	}

	@Override
	protected List<Button> orderButtons(Button cancel, Button notOk, Button ok) {
		return Arrays.asList(ok, notOk, cancel);
	}
}

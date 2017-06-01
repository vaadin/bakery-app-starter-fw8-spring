package com.vaadin.starter.bakery.ui.view;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.ui.PrototypeScope;

@SpringComponent
@PrototypeScope
public class AccessDeniedView extends AccessDeniedDesign implements NavigableView {

	@Override
	public void enter(ViewChangeEvent event) {
		// Nothing to do, just show the view
	}

}

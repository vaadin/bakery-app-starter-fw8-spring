package com.vaadin.template.orders.ui.components;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.data.entity.PickupLocation;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.ui.ComboBox;

@SpringComponent
@PrototypeScope
public class PickupLocationComboBox extends ComboBox<PickupLocation> {

	// Singleton data provider which knows which products are available and is
	// informed when they are updated, deleted or new ones are added
	@Autowired
	private PickupLocationComboBoxDataProvider dataProvider;

	public PickupLocationComboBox() {
		setEmptySelectionAllowed(false);
		setPlaceholder("Pickup location");
		setItemCaptionGenerator(PickupLocation::getName);
	}

	@PostConstruct
	private void init() {
		setDataProvider(dataProvider);
	}

}

package com.vaadin.template.orders.ui.components;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.ui.ComboBox;

@SpringComponent
@PrototypeScope
public class ProductComboBox extends ComboBox<Product> {

	// Singleton data provider which knows which products are available and is
	// informed when they are updated, deleted or new ones are added
	@Autowired
	private ProductComboBoxDataProvider dataProvider;

	public ProductComboBox() {
		setWidth("100%");
		setEmptySelectionAllowed(false);
		setPlaceholder("Product");
		setItemCaptionGenerator(Product::getName);
	}

	@PostConstruct
	public void init() {
		setDataProvider(dataProvider);
	}

}

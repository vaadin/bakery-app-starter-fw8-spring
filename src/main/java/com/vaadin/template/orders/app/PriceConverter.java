package com.vaadin.template.orders.app;

import java.text.NumberFormat;
import java.util.Locale;

import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.spring.annotation.SpringComponent;

@SpringComponent
public class PriceConverter extends StringToDoubleConverter {

	public PriceConverter() {
		super("Invalid price, please re-check the value");
	}

	@Override
	protected NumberFormat getFormat(Locale locale) {
		return NumberFormat.getCurrencyInstance(Locale.US);
	}
}

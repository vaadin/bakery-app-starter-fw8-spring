package com.vaadin.template.orders.ui.components;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

import com.vaadin.spring.annotation.SpringComponent;

@SpringComponent
public class DollarPriceFormatter implements Serializable {

    /**
     * Format the given double as a sum in dollars using the given locale.
     *
     * @param number
     *            the number to format
     * @param locale
     *            the locale to use to determine the format
     * @return a formatted number string
     */
    public String format(double number, Locale locale) {
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        return format.format(number);
    }

}

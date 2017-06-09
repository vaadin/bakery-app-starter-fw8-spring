package com.vaadin.starter.bakery.ui;

import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.addon.charts.model.style.Theme;
import com.vaadin.addon.charts.themes.ValoLightTheme;

/**
 * Theme for Vaadin Charts. See {@link ValoLightTheme} for a more complex theme.
 */
public class ChartsTheme extends Theme {

	public final static SolidColor COLOR1 = new SolidColor("#a56284");
	public final static SolidColor COLOR2 = new SolidColor("#6c6c93");
	public final static SolidColor COLOR3 = new SolidColor("#fb991c");

	public ChartsTheme() {
		setColors(COLOR1, COLOR2, COLOR3);
		getTitle().setColor(COLOR1);
		getTitle().setFontSize("inherit"); // inherit from CSS
	}
}

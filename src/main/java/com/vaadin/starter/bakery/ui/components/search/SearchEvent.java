package com.vaadin.starter.bakery.ui.components.search;

public class SearchEvent {
	private String searchString;
	private boolean includePast;

	public String getSearchString() {
		return searchString;
	}

	public boolean isIncludePast() {
		return includePast;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public void setIncludePast(boolean includePast) {
		this.includePast = includePast;
	}
}

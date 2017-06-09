package com.vaadin.starter.bakery.ui.components.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.vaadin.spring.annotation.PrototypeScope;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.Registration;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickShortcut;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@PrototypeScope
public class SearchField extends Composite {

	private TextField searchField;
	private Button searchButton;
	private CheckBox includePast;

	private Binder<SearchEvent> eventBinder;

	private List<SearchListener> searchListeners;

	public SearchField() {
		searchListeners = new ArrayList<>();

		eventBinder = new Binder<>();

		searchField = new TextField();
		searchField.setPlaceholder("Search");
		searchField.setId("searchField");

		includePast = new CheckBox("Include past");
		includePast.setStyleName(ValoTheme.CHECKBOX_SMALL);

		eventBinder.forField(searchField).bind(SearchEvent::getSearchString, SearchEvent::setSearchString);
		eventBinder.forField(includePast).bind(SearchEvent::isIncludePast, SearchEvent::setIncludePast);

		searchButton = new Button(VaadinIcons.SEARCH);
		searchButton.addClickListener(e -> onSearchClicked());
		searchButton.setId("searchButton");

		Panel panel = new Panel();
		panel.setStyleName(ValoTheme.PANEL_BORDERLESS);
		panel.setSizeUndefined();

		HorizontalLayout contentLayout = new HorizontalLayout(searchField, searchButton, includePast);
		contentLayout.setWidthUndefined();
		contentLayout.setComponentAlignment(includePast, Alignment.MIDDLE_RIGHT);
		panel.setContent(contentLayout);
		panel.addAction(new ClickShortcut(searchButton, KeyCode.ENTER, null));

		setCompositionRoot(panel);
	}

	public Registration addSerchListener(SearchListener listener) {
		searchListeners.add(Objects.requireNonNull(listener));

		return () -> searchListeners.remove(listener);
	}

	protected void onSearchClicked() {
		SearchEvent event = new SearchEvent();
		if (eventBinder.writeBeanIfValid(event)) {
			// I know! Should use searchListeners.forEach, but my eclipse
			// crashes (rofl)
			for (SearchListener listener : searchListeners) {
				listener.onSearch(event);
			}
		}
	}

	public void setSearchString(String searchString) {
		this.searchField.setValue(searchString);
	}

	public void setIncludePast(boolean includePast) {
		this.includePast.setValue(includePast);
	}
}

package com.vaadin.starter.bakery.ui.view.dashboard;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

public class BoardBox extends CssLayout {

	private CssLayout boardBox = new CssLayout();

	public BoardBox(Component component) {
		addStyleName("board-box-wrapper");
		addComponent(boardBox);
		boardBox.addStyleName("board-box");
		boardBox.setSizeFull();
		CssLayout inner = new CssLayout();
		inner.setSizeFull();
		inner.addStyleName("board-box-inner");
		inner.addComponent(component);
		boardBox.addComponent(inner);
	}

	public BoardBox(Component component, String styleName) {
		this(component);
		boardBox.addStyleName(styleName);
	}

	public void setNeedsAttention(boolean needsAttention) {
		boardBox.setStyleName("board-box-needs-attention", needsAttention);
	}
}

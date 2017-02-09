package com.vaadin.template.orders.ui.view.dashboard;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;

public class BoardLabel extends Label {

    private String header;
    private String content;

    public BoardLabel(String header, String content) {
        super("", ContentMode.HTML);
        addStyleName("center border");
        setSizeFull();
        setHeader(header);
        setContent(content);
    }

    private void setHeader(String header) {
        this.header = header;
        updateValue();
    }

    public void setContent(String content) {
        this.content = content;
        updateValue();
    }

    private void updateValue() {
        setValue("<h1>" + header + "</h1>" //
                + "<p>" //
                + "<h2>" + content + "</h2>");
    }

}

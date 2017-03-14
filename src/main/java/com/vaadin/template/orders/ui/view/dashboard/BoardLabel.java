package com.vaadin.template.orders.ui.view.dashboard;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;

public class BoardLabel extends Label {

    private String header;
    private String content;

    public BoardLabel(String header, String content) {
        super("", ContentMode.HTML);
        addStyleName("board-box-label");
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

    public void setNeedsAttention(boolean needsAttention) {
        setStyleName("board-box-needs-attention", needsAttention);
    }

    private void updateValue() {
        setValue("<div class=\"board-box-small-text\">" + header + "</div>" //
                + "<div class=\"board-box-big-text\">" + content + "</div>");
    }

}

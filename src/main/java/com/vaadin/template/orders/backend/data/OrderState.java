package com.vaadin.template.orders.backend.data;

import com.vaadin.shared.util.SharedUtil;

public enum OrderState {
    NEW, CONFIRMED, READY_FOR_PICKUP, DELIVERED, PROBLEM, CANCELLED;

    /**
     * Gets a version of the enum identifier in a human friendly format.
     *
     * @return a human friendly version of the identifier
     */
    public String getDisplayName() {
        return SharedUtil.upperCaseUnderscoreToHumanFriendly(name());
    }
}

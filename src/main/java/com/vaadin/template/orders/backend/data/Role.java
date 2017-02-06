package com.vaadin.template.orders.backend.data;

import java.util.stream.Stream;

public enum Role {
    BARISTA, BAKER, ADMIN;

    public static String[] getRoleNames() {
        Stream<String> roles = Stream.of(Role.values())
                .map(Role::getIdentifier);
        return roles.toArray(String[]::new);
    }

    /**
     * Gets the identifier of the role, used for access control.
     *
     * @return the string identifier of the role
     */
    public String getIdentifier() {
        return name();
    }
}

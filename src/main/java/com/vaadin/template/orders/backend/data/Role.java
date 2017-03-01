package com.vaadin.template.orders.backend.data;

public class Role {
    public static final String BARISTA = "barista";
    public static final String BAKER = "baker";
    public static final String ADMIN = "admin";
    public static final String[] ALL_ROLES = new String[] { BARISTA, BAKER,
            ADMIN };

    private Role() {
        // Static methods and fields only
    }

}

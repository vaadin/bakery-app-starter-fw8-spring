package com.vaadin.starter.bakery.backend.data;

import com.vaadin.shared.util.SharedUtil;

public enum OrderState {
	NEW, CONFIRMED, READY, DELIVERED, PROBLEM, CANCELLED;

	/**
	 * Gets a version of the enum identifier in a human friendly format.
	 *
	 * @return a human friendly version of the identifier
	 */
	public String getDisplayName() {
		return SharedUtil.upperCaseUnderscoreToHumanFriendly(name());
	}

	/**
	 * Gets a enum value for which {@link #getDisplayName()} returns the given
	 * string.
	 *
	 * @return the enum value with a matching display name
	 */
	public static OrderState forDisplayName(String displayName) {
		for (OrderState state : values()) {
			if (displayName.equals(state.getDisplayName())) {
				return state;
			}
		}
		return null;
	}
}

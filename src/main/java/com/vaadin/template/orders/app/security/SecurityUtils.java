package com.vaadin.template.orders.app.security;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.ui.UI;

/**
 * SecurityUtils takes care of all such static operations that have to do with
 * security and querying rights from different beans of the UI.
 * 
 * @author Peter / Vaadin
 */
public class SecurityUtils {

	/**
	 * Check if currently signed in user is in the role with given role name
	 * 
	 * @param role
	 * @return true if user is in the role, false otherwise
	 */
	public static boolean isCurrentUserInRole(String role) {
		return getUserRoles().stream().filter(roleName -> roleName.equals(Objects.requireNonNull(role))).findAny()
				.isPresent();
	}

	/**
	 * @return set of all such roles into which the currently signed in user
	 *         belongs to.
	 */
	public static Set<String> getUserRoles() {
		SecurityContext context = SecurityContextHolder.getContext();
		return context.getAuthentication().getAuthorities().stream().map(auth -> auth.getAuthority())
				.collect(Collectors.toSet());
	}

	/**
	 * Checks if access is granted for the current user for given secured view
	 * within given ui.
	 * 
	 * @param ui
	 * @param viewSecured
	 * @return true if access is granted, false otherwise.
	 */
	public static boolean isAccessGranted(UI ui, Secured viewSecured) {
		if (viewSecured == null) {
			return true;
		}

		return Arrays.asList(viewSecured.value()).stream().anyMatch(SecurityUtils::isCurrentUserInRole);
	}
}

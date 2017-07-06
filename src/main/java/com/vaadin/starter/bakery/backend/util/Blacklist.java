package com.vaadin.starter.bakery.backend.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.springframework.core.env.Environment;

/**
 * The annotated string must not match any of the blacklisted regexes found in
 * the specified {@link Environment} String[] property.
 * {@link BlacklistValidator} uses {@link String#matches(String)} for each
 * blacklisted "word"; i.e the regex must match the whole checked string.
 * <p>
 * 
 * <pre>
 * <code>
 * # application.properties
 * #
 * # Blacklist messages containing the word "profanity" (case insensitive) 
 * # or just "first" (and nothing else)
 * my.blacklist=.*(?i)profanity.*, first
 * </code>
 * </pre>
 * 
 * <pre>
 * <code>
 * // Message.java
 * &#64;Blacklist("my.blacklist")
 * String message;
 * </code>
 * </pre>
 * </p>
 *
 */
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BlacklistValidator.class)
@Documented
public @interface Blacklist {

	String message() default "Contains blacklisted word";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String value();

	@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {
		Blacklist[] value();
	}
}

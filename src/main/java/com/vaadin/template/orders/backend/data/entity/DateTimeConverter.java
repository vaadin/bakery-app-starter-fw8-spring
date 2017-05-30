package com.vaadin.template.orders.backend.data.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * JPA does not know how to handle Java 8 java.time.LocalDateTime so this
 * converts LocalDateTime into format it can handle.
 */
@Converter(autoApply = true)
public class DateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

	@Override
	public Timestamp convertToDatabaseColumn(LocalDateTime time) {
		if (time == null) {
			return null;
		}

		return Timestamp.valueOf(time);
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Timestamp time) {
		if (time == null) {
			return null;
		}

		return time.toLocalDateTime();
	}

}

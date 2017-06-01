package com.vaadin.starter.bakery.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HasLogger is a feature interface that provides Logging capability for anyone
 * implementing it where logger needs to operate in serializable environment
 * without being static.
 * 
 * @author Peter / Vaadin
 */
public interface HasLogger {

	default Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}
}

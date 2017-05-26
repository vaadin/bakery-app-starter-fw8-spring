package com.vaadin.template.orders.ui.eventbus;

import java.io.Serializable;
import java.util.function.Consumer;

import com.vaadin.shared.Registration;

/**
 * A view scoped event bus.
 */
public interface ViewEventBus extends Serializable {

	void publish(Object payload);

	<T> Registration subscribe(Class<T> payloadType, Consumer<T> listener);

}

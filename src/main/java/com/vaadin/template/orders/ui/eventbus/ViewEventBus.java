package com.vaadin.template.orders.ui.eventbus;

import java.util.function.Consumer;

import com.vaadin.shared.Registration;

public interface ViewEventBus {

	void publish(Object payload);

	<T> Registration subscribe(Class<T> payloadType, Consumer<T> listener);

}

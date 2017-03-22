package com.vaadin.template.orders.ui.eventbus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import com.vaadin.shared.Registration;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;

@SpringComponent
@ViewScope
public class ViewEventBusImpl implements ViewEventBus, Serializable {

    private Map<Class<?>, List<Consumer<?>>> listeners = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public void publish(Object payload) {
        Objects.requireNonNull(payload);

        Class<? extends Object> payloadType = payload.getClass();
        List<Consumer<?>> listenerList = listeners.get(payloadType);
        if (listenerList == null || listenerList.isEmpty()) {
            return;
        }
        Consumer<?>[] listenerListCopy = listenerList
                .toArray(new Consumer[listenerList.size()]);
        for (Consumer<?> c : listenerListCopy) {
            ((Consumer<Object>) c).accept(payload);
        }

    }

    @Override
    public <T> Registration subscribe(Class<T> payloadType,
            Consumer<T> listener) {
        List<Consumer<?>> listenerList = listeners.computeIfAbsent(payloadType,
                cls -> new ArrayList<>());
        listenerList.add(listener);
        return () -> unsubscribe(payloadType, listener);
    }

    private <T> void unsubscribe(Class<T> payloadType, Consumer<T> listener) {
        List<Consumer<?>> listenerList = listeners.get(payloadType);
        if (listenerList != null) {
            listenerList.remove(listener);
            if (listenerList.isEmpty()) {
                listeners.remove(payloadType);
            }
        }
    }

}

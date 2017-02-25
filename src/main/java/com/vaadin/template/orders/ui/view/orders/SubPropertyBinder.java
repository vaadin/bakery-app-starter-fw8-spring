package com.vaadin.template.orders.ui.view.orders;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.ui.TextField;

public class SubPropertyBinder {
    private SubPropertyBinder() {
        // Util methods only
    }

    public static void bind(Binder<?> binder, TextField field, String property,
            Class<?> containerType) {
        String finalProperty = resolveFinalProperty(property);
        binder.forField(field)
                .withValidator(new BeanValidator(containerType, finalProperty))
                .bind(order -> (String) getValue(order, property, false),
                        (order, value) -> setValue(order, value, property));
    }

    private static String resolveFinalProperty(String property) {
        return property.substring(property.lastIndexOf('.') + 1);
    }

    public static Object getValue(Object object, String propertyPath,
            boolean populateNulls) {
        if (object == null) {
            return null;
        }

        String property;
        String remainingPath = null;
        if (propertyPath.contains(".")) {
            int firstDot = propertyPath.indexOf('.');
            property = propertyPath.substring(0, firstDot);
            remainingPath = propertyPath.substring(firstDot + 1);
        } else {
            property = propertyPath;
        }

        try {
            PropertyDescriptor pd = getDescriptor(object.getClass(), property);

            Object subObject = pd.getReadMethod().invoke(object);
            if (subObject == null && populateNulls) {
                subObject = pd.getPropertyType().newInstance();
                pd.getWriteMethod().invoke(object, subObject);
            }
            if (remainingPath == null) {
                return subObject;
            } else {
                return getValue(subObject, remainingPath, populateNulls);
            }
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | InstantiationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static PropertyDescriptor getDescriptor(
            Class<? extends Object> class1, String property) {
        try {
            Optional<PropertyDescriptor> pd = Arrays
                    .stream(Introspector.getBeanInfo(class1)
                            .getPropertyDescriptors())
                    .filter(desc -> desc.getName().equals(property))
                    .findFirst();
            if (!pd.isPresent()) {
                throw new IllegalArgumentException("No property named "
                        + property + " was found in " + class1);
            }

            return pd.get();
        } catch (IntrospectionException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void setValue(Object object, String value,
            String propertyPath) {
        if (!propertyPath.contains(".")) {
            throw new IllegalArgumentException(
                    "property path must contain a dot");
        }

        int lastDot = propertyPath.lastIndexOf('.');
        String intermediatePath = propertyPath.substring(0, lastDot);
        String finalPath = propertyPath.substring(lastDot + 1);

        Object o = getValue(object, intermediatePath, true);
        PropertyDescriptor pd = getDescriptor(o.getClass(), finalPath);
        try {
            pd.getWriteMethod().invoke(o, value);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

}

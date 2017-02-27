package com.vaadin.template.orders.ui;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;

import com.vaadin.template.orders.app.SpringContextHolder;
import com.vaadin.ui.Component;
import com.vaadin.ui.declarative.Design.DefaultComponentFactory;
import com.vaadin.ui.declarative.DesignContext;

public class SpringCompatibleComponentFactory extends DefaultComponentFactory {

    @Override
    public Component createComponent(String fullyQualifiedClassName,
            DesignContext context) {
        ApplicationContext appContext = SpringContextHolder.getAppContext();
        Class<? extends Component> componentClass = resolveComponentClass(
                fullyQualifiedClassName, context);
        String[] beanNames = BeanFactoryUtils
                .beanNamesForTypeIncludingAncestors(appContext, componentClass);
        for (String beanName : beanNames) {
            if (appContext.getAutowireCapableBeanFactory()
                    .getType(beanName) == componentClass) {
                return appContext.getBean(componentClass);
            }
        }

        return super.createComponent(fullyQualifiedClassName, context);
    }

}

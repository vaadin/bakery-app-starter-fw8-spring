package com.vaadin.template.orders.app;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import com.vaadin.ui.Component;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.declarative.Design.DefaultComponentFactory;
import com.vaadin.ui.declarative.DesignContext;

@Configuration
public class DesignAutowireConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        Design.setComponentFactory(new SpringCompatibleComponentFactory());
    }

    public class SpringCompatibleComponentFactory
            extends DefaultComponentFactory {

        @Override
        public Component createComponent(String fullyQualifiedClassName,
                DesignContext context) {
            Class<? extends Component> componentClass = resolveComponentClass(
                    fullyQualifiedClassName, context);
            String[] beanNames = BeanFactoryUtils
                    .beanNamesForTypeIncludingAncestors(applicationContext,
                            componentClass);
            for (String beanName : beanNames) {
                if (applicationContext.getAutowireCapableBeanFactory()
                        .getType(beanName) == componentClass) {
                    return applicationContext.getBean(componentClass);
                }
            }

            return super.createComponent(fullyQualifiedClassName, context);
        }

    }

}

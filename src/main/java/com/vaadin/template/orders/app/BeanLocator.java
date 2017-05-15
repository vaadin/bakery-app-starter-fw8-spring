package com.vaadin.template.orders.app;

import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * BeanLocator is singleton Spring bean that is capable of finding required bean
 * from Spring's application context. With BeanLocator an existing reference may
 * be used or a fresh reference can be acquired from the app context.
 * 
 * @author Peter / Vaadin
 *
 */
@Component
public class BeanLocator {

	private static BeanLocator instance;

	@Autowired
	private ApplicationContext context;

	@PostConstruct
	protected void initialize() {
		instance = this;
	}

	/**
	 * Uses BeanLocator for given bean instance. Given instance may be null.
	 * 
	 * @param bean
	 * @return BeanResolution provided for given bean.
	 */
	public static <T> BeanResolution<T> use(T bean) {
		return new BeanResolution<T>(bean, instance.context);
	}

	/**
	 * BeanResolution describes existing bean instance or bean resolution
	 * ability from associated app context.
	 * 
	 * @author Peter / Vaadin
	 *
	 * @param <T>
	 */
	public static class BeanResolution<T> {
		private T bean;
		private ApplicationContext context;

		protected BeanResolution(T bean, ApplicationContext context) {
			this.bean = bean;
			this.context = Objects.requireNonNull(context);
		}

		/**
		 * If bean used by the {@link BeanLocator} is null, the given type may
		 * be used for discovering a fresh reference to the bean of given type.
		 * 
		 * @param type
		 * @return bean of given type or the bean instance that was passed in
		 *         with {@link BeanLocator#use(Object)}
		 */
		public T orElseFindInstance(Class<T> type) {
			return bean == null ? context.getBean(type) : bean;
		}
	}
}

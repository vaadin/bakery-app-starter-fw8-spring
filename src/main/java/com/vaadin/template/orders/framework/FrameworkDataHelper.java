package com.vaadin.template.orders.framework;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.vaadin.data.provider.Query;

public class FrameworkDataHelper {

	private FrameworkDataHelper() {
		// Class only contains static helpers
	}

	public static <T, F> Pageable getPageable(Query<T, F> q) {
		return new PageRequest(0, q.getOffset() + q.getLimit());
	}

}

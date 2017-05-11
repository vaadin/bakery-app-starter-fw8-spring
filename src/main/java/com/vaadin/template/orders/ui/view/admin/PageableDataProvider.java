package com.vaadin.template.orders.ui.view.admin;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;

public abstract class PageableDataProvider<T, F> extends AbstractBackEndDataProvider<T, F> {

	@Override
	protected Stream<T> fetchFromBackEnd(Query<T, F> query) {
		Pageable pageable = getPageable(query);
		Page<T> result = fetchFromBackEnd(query, pageable);
		return fromPageable(result, pageable, query);
	}

	protected abstract Page<T> fetchFromBackEnd(Query<T, F> query, Pageable pageable);

	private static <T, F> Pageable getPageable(Query<T, F> q) {
		return new PageRequest(0, q.getOffset() + q.getLimit());
	}

	private static <T> Stream<T> fromPageable(Page<T> result, Pageable pageable, Query<T, ?> query) {
		List<T> items = result.getContent();

		int firstRequested = query.getOffset();
		int nrRequested = query.getLimit();
		int firstReturned = pageable.getPageNumber();
		int firstReal = firstRequested - firstReturned;
		int afterLastReal = firstReal + nrRequested;
		if (afterLastReal > items.size()) {
			afterLastReal = items.size();
		}
		return items.subList(firstReal, afterLastReal).stream();
	}

}

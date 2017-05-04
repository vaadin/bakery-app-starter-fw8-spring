package com.vaadin.template.orders.ui.view.admin;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.template.orders.framework.FrameworkDataHelper;

public abstract class PageableDataProvider<T, F> extends AbstractBackEndDataProvider<T, F> {

	@Override
	protected Stream<T> fetchFromBackEnd(Query<T, F> query) {
		int firstRequested = query.getOffset();
		int nrRequested = query.getLimit();
		Pageable pageable = FrameworkDataHelper.getPageable(query);

		List<T> items = fetchFromBackEnd(query, pageable).getContent();
		int firstReturned = pageable.getPageNumber();
		int firstReal = firstRequested - firstReturned;
		int afterLastReal = firstReal + nrRequested;
		if (afterLastReal > items.size()) {
			afterLastReal = items.size();
		}
		return items.subList(firstReal, afterLastReal).stream();

	}

	protected abstract Page<T> fetchFromBackEnd(Query<T, F> query, Pageable pageable);

}

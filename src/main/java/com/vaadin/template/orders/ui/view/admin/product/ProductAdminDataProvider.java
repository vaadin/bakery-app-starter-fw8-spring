package com.vaadin.template.orders.ui.view.admin.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaadin.data.provider.Query;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.ProductRepository;
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.ui.PrototypeScope;
import com.vaadin.template.orders.ui.view.admin.PageableDataProvider;

@SpringComponent
@PrototypeScope
public class ProductAdminDataProvider extends PageableDataProvider<Product, Object> {

	@Autowired
	private ProductRepository repository;
	private String filter = null;

	@Override
	protected Page<Product> fetchFromBackEnd(Query<Product, Object> query, Pageable pageable) {
		if (filter == null) {
			return repository.findByOrderByName(pageable);
		} else {
			return repository.findByNameLikeIgnoreCaseOrderByName(filter, pageable);
		}
	}

	@Override
	protected int sizeInBackEnd(Query<Product, Object> query) {
		if (filter == null) {
			return (int) repository.count();
		} else {
			return repository.countByNameLikeIgnoreCase(filter);
		}
	}

	public void setFilter(String filter) {
		if ("".equals(filter)) {
			this.filter = null;
		} else {
			this.filter = "%" + filter + "%";
		}
		refreshAll();
	}

	@Override
	public Object getId(Product item) {
		return item.getId();
	}

}
package com.vaadin.template.orders.ui.view.admin.product;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.ValueContext;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.template.orders.app.DollarPriceConverter;
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.ui.view.OrdersView;
import com.vaadin.template.orders.ui.view.admin.AbstractCrudView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;

@SpringView
public class ProductAdminView extends AbstractCrudView<Product> implements OrdersView {

	@Autowired
	private ProductAdminPresenter presenter;

	private final ProductAdminViewDesign userAdminViewDesign;

	@Autowired
	private DollarPriceConverter priceToStringConverter;

	public ProductAdminView() {
		super(Product.class);
		userAdminViewDesign = new ProductAdminViewDesign();
	}

	@Override
	@PostConstruct
	public void init() {
		super.init();
		presenter.init(this);
		getGrid().setColumns("name", "price");
		getGrid().removeColumn("price");
		getGrid().addColumn(product -> priceToStringConverter.convertToPresentation(product.getPrice(),
				new ValueContext(getGrid()))).setSortProperty("price");
		getBinder().forField(getViewComponent().price).withConverter(priceToStringConverter).bind("price");
		getBinder().bindInstanceFields(getViewComponent());

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// Nothing to do here
	}

	@Override
	public ProductAdminViewDesign getViewComponent() {
		return userAdminViewDesign;
	}

	@Override
	protected ProductAdminPresenter getPresenter() {
		return presenter;
	}

	@Override
	protected Grid<Product> getGrid() {
		return getViewComponent().list;
	}

	@Override
	protected void setGrid(Grid<Product> grid) {
		getViewComponent().list = grid;
	}

	@Override
	protected Component getForm() {
		return getViewComponent().form;
	}

	@Override
	protected Button getAdd() {
		return getViewComponent().add;
	}

	@Override
	protected Button getCancel() {
		return getViewComponent().cancel;
	}

	@Override
	protected Button getDelete() {
		return getViewComponent().delete;
	}

	@Override
	protected Button getUpdate() {
		return getViewComponent().update;
	}

	@Override
	protected TextField getSearch() {
		return getViewComponent().search;
	}

	@Override
	protected Focusable getFirstFormField() {
		return getViewComponent().name;
	}

}
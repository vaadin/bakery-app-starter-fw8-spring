package com.vaadin.starter.bakery.ui.view.admin.product;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.ValueContext;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.ui.util.DollarPriceConverter;
import com.vaadin.starter.bakery.ui.view.admin.AbstractCrudView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;

@SpringView
public class ProductAdminView extends AbstractCrudView<Product> {

	private final ProductAdminPresenter presenter;

	private final ProductAdminViewDesign userAdminViewDesign;

	private final DollarPriceConverter priceToStringConverter;

	private static final String PRICE_PROPERTY = "price";

	@Autowired
	public ProductAdminView(ProductAdminPresenter presenter, DollarPriceConverter priceToStringConverter) {
		super(Product.class);
		this.presenter = presenter;
		this.priceToStringConverter = priceToStringConverter;
		userAdminViewDesign = new ProductAdminViewDesign();
	}

	@Override
	@PostConstruct
	public void init() {
		super.init();
		presenter.init(this);
		getGrid().setColumns("name", PRICE_PROPERTY);
		getGrid().removeColumn(PRICE_PROPERTY);
		getGrid().addColumn(product -> priceToStringConverter.convertToPresentation(product.getPrice(),
				new ValueContext(getGrid()))).setSortProperty(PRICE_PROPERTY);
		getBinder().forField(getViewComponent().price).withConverter(priceToStringConverter).bind(PRICE_PROPERTY);
		getBinder().bindInstanceFields(getViewComponent());

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
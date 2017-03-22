package com.vaadin.template.orders.ui.view.admin.product;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.ValueContext;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.template.orders.app.PriceConverter;
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.ui.view.admin.AbstractCrudView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;

@SpringView
public class ProductAdminView extends AbstractCrudView<Product>
        implements View {

    @Autowired
    private ProductAdminPresenter presenter;

    private final ProductAdminViewDesign userAdminViewDesign;

    @Autowired
    private PriceConverter priceToStringConverter;

    public ProductAdminView() {
        super(Product.class);
        userAdminViewDesign = new ProductAdminViewDesign();
    }

    @Override
    @PostConstruct
    public void init() {
        super.init();
        getGrid().setColumns("name", "price");
        getGrid().removeColumn("price");
        getGrid().addColumn(
                product -> priceToStringConverter.convertToPresentation(
                        product.getPrice(), new ValueContext(getGrid())));
        getBinder().forField(getComponent().price)
                .withConverter(priceToStringConverter).bind("price");
        getBinder().bindInstanceFields(getComponent());

        presenter.init(this);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // Nothing to do here
    }

    @Override
    public ProductAdminViewDesign getComponent() {
        return userAdminViewDesign;
    }

    @Override
    protected ProductAdminPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected Grid<Product> getGrid() {
        return getComponent().list;
    }

    @Override
    protected void setGrid(Grid<Product> grid) {
        getComponent().list = grid;
    }

    @Override
    protected Component getForm() {
        return getComponent().form;
    }

    @Override
    protected Button getAdd() {
        return getComponent().add;
    }

    @Override
    protected Button getCancel() {
        return getComponent().cancel;
    }

    @Override
    protected Button getDelete() {
        return getComponent().delete;
    }

    @Override
    protected Button getUpdate() {
        return getComponent().update;
    }

    @Override
    protected TextField getSearch() {
        return getComponent().search;
    }

}
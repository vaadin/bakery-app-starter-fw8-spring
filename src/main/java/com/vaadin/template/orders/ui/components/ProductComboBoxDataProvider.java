package com.vaadin.template.orders.ui.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vaadin.data.provider.Query;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.ProductRepository;
import com.vaadin.template.orders.backend.data.entity.Product;
import com.vaadin.template.orders.ui.view.admin.PageableDataProvider;

@SpringComponent
public class ProductComboBoxDataProvider
        extends PageableDataProvider<Product, String> {

    private final ProductRepository productRepository;

    @Autowired
    public ProductComboBoxDataProvider(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private String getNameFilter(Query<Product, String> query) {
        if (query.getFilter().isPresent()) {
            return "%" + query.getFilter().get() + "%";
        } else {
            return "%";
        }
    }

    @Override
    protected Page<Product> fetchFromBackEnd(Query<Product, String> query,
            Pageable pageable) {
        return productRepository.findByNameLikeIgnoreCaseOrderByName(
                getNameFilter(query), pageable);
    }

    @Override
    protected int sizeInBackEnd(Query<Product, String> query) {
        return productRepository
                .countByNameLikeIgnoreCase(getNameFilter(query));
    }

}

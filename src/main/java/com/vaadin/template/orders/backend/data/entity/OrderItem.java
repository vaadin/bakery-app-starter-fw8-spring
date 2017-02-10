package com.vaadin.template.orders.backend.data.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @NotNull
    private Product product;
    private int quantity;
    private String comment;

    protected OrderItem() {
        // Empty constructor is needed by Spring Data / JPA
    }

    public OrderItem(Product product, int quantity) {
        Objects.requireNonNull(product);
        Objects.requireNonNull(quantity);
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}

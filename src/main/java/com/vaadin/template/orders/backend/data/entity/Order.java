package com.vaadin.template.orders.backend.data.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity(name = "OrderInfo") // "Order" is a reserved word
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    // LocalDateTime?
    private Date due;
    @OneToOne
    private PickupLocation pickupLocation;
    @OneToOne
    private Customer customer;
    @OneToMany
    private List<OrderItem> items;
    private boolean paid;
    @OneToMany
    private List<HistoryItem> history;

    protected Order() {
        // Empty constructor is needed by Spring Data / JPA
    }

    public Long getId() {
        return id;
    }

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    public PickupLocation getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(PickupLocation pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public List<HistoryItem> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryItem> history) {
        this.history = history;
    }

}

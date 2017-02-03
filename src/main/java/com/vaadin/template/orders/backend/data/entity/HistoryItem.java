package com.vaadin.template.orders.backend.data.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.vaadin.template.orders.backend.data.OrderState;

@Entity
public class HistoryItem {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private OrderState newState;
    private String message;

    @NotNull
    private LocalDateTime timestamp;
    @OneToOne
    private User createdBy;

    public HistoryItem() {
        // Empty constructor is needed by Spring Data / JPA
    }

    public Long getId() {
        return id;
    }

    public OrderState getNewState() {
        return newState;
    }

    public void setNewState(OrderState newState) {
        this.newState = newState;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

}

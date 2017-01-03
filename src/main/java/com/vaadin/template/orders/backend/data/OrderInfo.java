package com.vaadin.template.orders.backend.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OrderInfo {

	@Id
	@GeneratedValue
	private Long id;

	private String description;

	protected OrderInfo() {
	}

	public OrderInfo(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

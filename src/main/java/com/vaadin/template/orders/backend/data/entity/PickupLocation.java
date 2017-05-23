package com.vaadin.template.orders.backend.data.entity;

import javax.persistence.Entity;

@Entity
public class PickupLocation extends AbstractEntity {

	private String name;

	public PickupLocation() {
		// Empty constructor is needed by Spring Data / JPA
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

package com.vaadin.template.orders.backend.data.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PickupLocation implements Serializable {

	@Id
	@GeneratedValue
	private Long id;
	private String name;

	public PickupLocation() {
		// Empty constructor is needed by Spring Data / JPA
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

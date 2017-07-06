package com.vaadin.starter.bakery.backend.data.entity;

import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.vaadin.starter.bakery.backend.util.Blacklist;

@Entity
public class Product extends AbstractEntity {

	@Size(max = 255)
	@Blacklist("demo.blacklist")
	private String name;

	// Real price * 100 as an int to avoid rounding errors
	@Min(0)
	@Max(100000)
	private int price;

	public Product() {
		// Empty constructor is needed by Spring Data / JPA
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

}

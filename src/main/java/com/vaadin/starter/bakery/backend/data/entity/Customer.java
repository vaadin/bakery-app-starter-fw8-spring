package com.vaadin.starter.bakery.backend.data.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.vaadin.starter.bakery.backend.util.Blacklist;

@Entity
public class Customer extends AbstractEntity {

	@NotNull
	@NotEmpty
	@Size(max = 255)
	@Blacklist("demo.blacklist")
	private String fullName;
	@NotNull
	@NotEmpty
	@Size(max = 255)
	@Blacklist("demo.blacklist")
	private String phoneNumber;
	@Size(max = 255)
	@Blacklist("demo.blacklist")
	private String details;

	public Customer() {
		// Empty constructor is needed by Spring Data / JPA
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}

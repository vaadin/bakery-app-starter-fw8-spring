package com.vaadin.template.orders.backend.data.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Customer implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@NotEmpty
	private String fullName;
	@NotNull
	@NotEmpty
	private String phoneNumber;
	private String details;

	public Customer() {
		// Empty constructor is needed by Spring Data / JPA
	}

	public Long getId() {
		return id;
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

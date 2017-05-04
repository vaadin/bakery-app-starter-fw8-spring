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
	private String firstName;
	@NotNull
	@NotEmpty
	private String lastName;
	@NotNull
	@NotEmpty
	private String phoneNumber;
	@NotNull
	@NotEmpty
	private String email;
	private String details;

	public Customer() {
		// Empty constructor is needed by Spring Data / JPA
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}

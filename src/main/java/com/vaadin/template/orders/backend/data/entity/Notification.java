package com.vaadin.template.orders.backend.data.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Notification implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	private String caption;
	@NotNull
	private LocalDateTime timestamp;
	private String message;
	@NotNull
	@OneToOne
	private User updatedBy;

	public Notification() {
		// Empty constructor is needed by Spring Data / JPA
	}

	public Long getId() {
		return id;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}

}

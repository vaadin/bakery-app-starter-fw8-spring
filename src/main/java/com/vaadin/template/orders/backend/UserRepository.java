package com.vaadin.template.orders.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.template.orders.backend.data.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
}

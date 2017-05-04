package com.vaadin.template.orders.backend.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.template.orders.backend.UserRepository;
import com.vaadin.template.orders.backend.data.entity.User;
import com.vaadin.template.orders.ui.OrdersUI;

@SpringComponent
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User getCurrentUser() {
		return userRepository.findByEmail(OrdersUI.get().getUsername());
	}

}

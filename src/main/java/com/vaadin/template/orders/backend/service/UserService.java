package com.vaadin.template.orders.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vaadin.template.orders.backend.UserRepository;
import com.vaadin.template.orders.backend.data.entity.User;
import com.vaadin.template.orders.ui.OrdersUI;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User getCurrentUser() {
		return userRepository.findByEmail(OrdersUI.get().getUsername());
	}

	public Page<User> find(Pageable pageable) {
		return userRepository.findByOrderByEmail(pageable);
	}

	public long count() {
		return userRepository.count();
	}

	public Page<User> findAnyMatching(Optional<String> filter, Pageable pageable) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return userRepository.findByEmailLikeIgnoreCaseOrNameLikeIgnoreCaseOrderByEmail(repositoryFilter,
					repositoryFilter, pageable);
		} else {
			return find(pageable);
		}
	}

	public long countAnyMatching(Optional<String> filter) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return userRepository.countByEmailLikeIgnoreCaseOrNameLikeIgnoreCase(repositoryFilter, repositoryFilter);
		} else {
			return userRepository.count();
		}
	}

	public User get(Long id) {
		return userRepository.findOne(id);
	}

	public void delete(Long id) {
		userRepository.delete(id);
	}

	public User save(User product) {
		return userRepository.save(product);
	}
}

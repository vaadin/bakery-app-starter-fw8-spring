package com.vaadin.template.orders.backend.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vaadin.template.orders.app.BeanLocator;
import com.vaadin.template.orders.backend.UserRepository;
import com.vaadin.template.orders.backend.data.entity.User;
import com.vaadin.template.orders.ui.OrdersUI;

@Service
public class UserService {

	private transient UserRepository userRepository;

	public User getCurrentUser() {
		return getUserRepository().findByEmail(OrdersUI.get().getUsername());
	}

	public Page<User> find(Pageable pageable) {
		return getUserRepository().findByOrderByEmail(pageable);
	}

	public long count() {
		return getUserRepository().count();
	}

	public Page<User> findAnyMatching(Optional<String> filter, Pageable pageable) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return getUserRepository().findByEmailLikeIgnoreCaseOrNameLikeIgnoreCaseOrderByEmail(repositoryFilter,
					repositoryFilter, pageable);
		} else {
			return find(pageable);
		}
	}

	public long countAnyMatching(Optional<String> filter) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return getUserRepository().countByEmailLikeIgnoreCaseOrNameLikeIgnoreCase(repositoryFilter,
					repositoryFilter);
		} else {
			return getUserRepository().count();
		}
	}

	public User get(Long id) {
		return getUserRepository().findOne(id);
	}

	public void delete(Long id) {
		getUserRepository().delete(id);
	}

	public User save(User product) {
		return getUserRepository().save(product);
	}

	protected UserRepository getUserRepository() {
		return userRepository = BeanLocator.use(userRepository).orElseFindInstance(UserRepository.class);
	}
}

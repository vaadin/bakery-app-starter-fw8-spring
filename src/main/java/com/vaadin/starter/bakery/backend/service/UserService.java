package com.vaadin.starter.bakery.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.starter.bakery.backend.UserRepository;
import com.vaadin.starter.bakery.backend.data.entity.User;

@Service
public class UserService extends CrudService<User> {

	private static final String MODIFY_LOCKED_USER_NOT_PERMITTED = "User has been locked and cannot be modified or deleted";
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public User findByEmail(String email) {
		return getRepository().findByEmail(email);
	}

	@Override
	public Page<User> findAnyMatching(Optional<String> filter, Pageable pageable) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return getRepository().findByEmailLikeIgnoreCaseOrNameLikeIgnoreCaseOrRoleLikeIgnoreCase(repositoryFilter,
					repositoryFilter, repositoryFilter, pageable);
		} else {
			return find(pageable);
		}
	}

	@Override
	public long countAnyMatching(Optional<String> filter) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return getRepository().countByEmailLikeIgnoreCaseOrNameLikeIgnoreCase(repositoryFilter, repositoryFilter);
		} else {
			return getRepository().count();
		}
	}

	@Override
	protected UserRepository getRepository() {
		return userRepository;
	}

	@Override
	public Page<User> find(Pageable pageable) {
		return getRepository().findBy(pageable);
	}

	public String encodePassword(String value) {
		return passwordEncoder.encode(value);
	}

	@Override
	@Transactional
	public User save(User entity) {
		throwIfUserLocked(entity.getId());
		return getRepository().save(entity);
	}

	@Override
	@Transactional
	public void delete(long userId) {
		throwIfUserLocked(userId);
		getRepository().delete(userId);
	}

	private void throwIfUserLocked(Long userId) {
		if (userId == null) {
			return;
		}

		User dbUser = getRepository().findOne(userId);
		if (dbUser.isLocked()) {
			throw new UserFriendlyDataException(MODIFY_LOCKED_USER_NOT_PERMITTED);
		}
	}

}

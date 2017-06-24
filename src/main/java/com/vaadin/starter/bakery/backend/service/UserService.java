package com.vaadin.starter.bakery.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vaadin.starter.bakery.app.BeanLocator;
import com.vaadin.starter.bakery.app.security.SecurityUtils;
import com.vaadin.starter.bakery.backend.UserRepository;
import com.vaadin.starter.bakery.backend.data.entity.User;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements CrudService<User> {

	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public User getCurrentUser() {
		return getRepository().findByEmail(SecurityUtils.getUsername());
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
	public UserRepository getRepository() {
		return BeanLocator.find(UserRepository.class);
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
	    if(entity.getId() != null) {
            User dbUser = getRepository().getOne(entity.getId());
            if(dbUser.isLocked()){
                throw new DataIntegrityViolationException("Tried to save User entity, but it has been locked " +
                        "and changes through API is not permitted");
            }
        }
		if(entity.isLocked()){
			throw new DataIntegrityViolationException("Tried to save User entity, but it has been locked and changes" +
                    " through API is not permitted");
		}
		return getRepository().save(entity);
	}

    @Override
    @Transactional
    public void delete(long id) {
        User user = getRepository().getOne(id);
        if(user != null && user.isLocked()){
            throw new DataIntegrityViolationException("User has been locked from editing.");
        }
        getRepository().delete(id);
    }
}

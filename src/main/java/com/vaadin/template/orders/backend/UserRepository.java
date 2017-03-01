package com.vaadin.template.orders.backend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.template.orders.backend.data.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    Page<User> findByOrderByEmail(Pageable pageable);

    Page<User> findByEmailLikeIgnoreCaseOrNameLikeIgnoreCaseOrderByEmail(
            String emailLike, String nameLike, Pageable pageable);

    long countByEmailLikeIgnoreCaseOrNameLikeIgnoreCase(String emailLike,
            String nameLike);
}

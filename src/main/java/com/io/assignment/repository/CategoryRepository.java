package com.io.assignment.repository;

import com.io.assignment.entity.Category;
import com.io.assignment.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByName(String name);

    Page<Category> findByUser(User user, Pageable pageable);

    boolean existsByName(String name);
}

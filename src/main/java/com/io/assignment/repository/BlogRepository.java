package com.io.assignment.repository;

import com.io.assignment.entity.Blog;
import com.io.assignment.entity.Category;
import com.io.assignment.entity.Tag;
import com.io.assignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    Page<Blog> findByCategory(Category category, Pageable pageable);

    Page<Blog> findByTags(Tag tag, Pageable pageable);

    Optional<Blog> findByTitle(String title);

    void deleteAllByCategory(Category category);

    Page<Blog> findByUser(User user, Pageable pageable);

    Integer countByUser(User user);
}

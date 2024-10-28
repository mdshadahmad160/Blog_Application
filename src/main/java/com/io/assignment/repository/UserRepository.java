package com.io.assignment.repository;

import com.io.assignment.entity.Blog;
import com.io.assignment.entity.Category;
import com.io.assignment.entity.Comment;
import com.io.assignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {


    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User findByBlogs(Blog blog);

    User findByCategories(Category category);

    User findByComments(Comment comment);


}

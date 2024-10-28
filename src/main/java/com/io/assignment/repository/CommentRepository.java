package com.io.assignment.repository;

import com.io.assignment.entity.Blog;
import com.io.assignment.entity.Comment;
import com.io.assignment.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface CommentRepository extends JpaRepository<Comment,Long> {


    Page<Comment> findByBlog(Blog blog, Pageable pageable);

    void deleteAllByBlog(Blog blog);

    Page<Comment> findByUser(User user, Pageable pageable);

}

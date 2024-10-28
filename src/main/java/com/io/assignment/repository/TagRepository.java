package com.io.assignment.repository;

import com.io.assignment.entity.Blog;
import com.io.assignment.entity.Tag;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface TagRepository extends JpaRepository<Tag,Long> {

    Page<Tag> findByBlogs(Blog blog, Pageable pageable);

    Optional<Tag> findByName(String name);

    boolean existsByName(String name);

    List<Tag> findAllByBlogs(Blog blog);

}

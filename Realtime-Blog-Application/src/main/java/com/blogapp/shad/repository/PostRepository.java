package com.blogapp.shad.repository;
import com.blogapp.shad.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {


    List<Post> findByCategoryId(Long categoryId);

  @Query("SELECT p FROM Post p Where "+
         "p.title LIKE CONCAT('%', :query, '%')" +
          "Or p.description LIKE CONCAT('%', :query, '%')" +
          "Or p.content LIKE CONCAT('%', :query, '%')")

    List<Post> searchProducts(String query);





}

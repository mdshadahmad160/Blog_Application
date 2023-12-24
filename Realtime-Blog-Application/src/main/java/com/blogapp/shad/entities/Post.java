package com.blogapp.shad.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts",uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})})
public class Post {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   @Column(nullable = false)
    private String title;
   @Column(nullable = false)
    private String description;
   @Column(nullable = false)
    private String content;

  @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,orphanRemoval = true)
   private Set<Comment> comments=new HashSet<>();
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

}

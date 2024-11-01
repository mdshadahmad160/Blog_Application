package com.io.assignment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "blog_tag",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "blog_id")
    )
    private List<Blog> blogs;


    @JsonIgnore
    public List<Blog> getBlogs() {
        return blogs == null ? null : new ArrayList<>(this.blogs);
    }

    public void setBlogs(List<Blog> blogs) {
        if(blogs == null) {
            this.blogs = null;
        }else {
            this.blogs = blogs;
        }
    }

    public void removeBlog(Blog blog) {
        this.getBlogs().remove(blog);
        blog.setTags(null);
    }


}

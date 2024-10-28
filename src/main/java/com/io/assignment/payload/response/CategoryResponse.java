package com.io.assignment.payload.response;

import com.io.assignment.entity.Blog;
import com.io.assignment.entity.UserDateAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryResponse extends UserDateAudit {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String description;

    private String title;

    private Long userId;

    private List<Blog> blogs;


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
}

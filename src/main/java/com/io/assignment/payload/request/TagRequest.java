package com.io.assignment.payload.request;

import com.io.assignment.entity.Blog;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TagRequest {

    private String name;

    private String description;

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

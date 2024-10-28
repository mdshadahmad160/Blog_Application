package com.io.assignment.payload.response;

import com.io.assignment.entity.Comment;
import com.io.assignment.entity.Tag;
import com.io.assignment.entity.UserDateAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class BlogResponse  extends UserDateAudit {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String body;

    private String description;

    private String image;

    private Long views;

    private Boolean published;

    private List<Comment> comments;

    private Long categoryId;

    private Long userId;

    private List<Tag> tags;


    public List<Comment> getComments() {
        return comments == null ? null : new ArrayList<>(this.comments);
    }

    public void setComments(List<Comment> comments) {
        if (comments == null) {
            this.comments = null;
        } else {
            this.comments = comments;
        }

    }

    public List<Tag> getTags() {
        return tags == null ? null : new ArrayList<>(this.tags);
    }

    public void setTags(List<Tag> tags) {
        if (tags == null) {
            this.tags = null;
        } else {
            this.tags = tags;
        }
    }
}

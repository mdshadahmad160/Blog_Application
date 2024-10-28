package com.io.assignment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "blogs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog extends UserDateAudit {

    private static final long serialVersionUID = 1L;



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "body")
    private String body;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "views")
    private Long views;

    @Column(name = "published")
    private Boolean published;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "blog_tag",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public List<Comment> getComments() {
        return comments == null ? null : new ArrayList<>(this.comments);
    }

    public void setComments(List<Comment> comments) {
        if(comments == null) {
            this.comments = null;
        }else {
            this.comments = comments;
        }

    }

    public List<Tag> getTags() {
        return tags == null ? null : new ArrayList<>(this.tags);
    }

    public void setTags(List<Tag> tags) {
        if(tags == null) {
            this.tags = null;
        }else {
            this.tags = tags;
        }
    }


}

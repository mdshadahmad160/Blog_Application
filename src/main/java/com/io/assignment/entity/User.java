package com.io.assignment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@ToString
public class User extends UserDateAudit {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "password")
    private String password;

    @Column(name = "address")
    private String address;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "image")
    private String image;

    @Column(name = "enabled")
    private Boolean enabled;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Blog> blogs;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<Role> roles;

    @JsonIgnore
    public List<Blog> getBlogs() {
        return blogs == null ? null : new ArrayList<>(this.blogs);
    }

    public void setBlogs(List<Blog> blogs) {
        if (blogs == null) {
            this.blogs = null;
        } else {
            this.blogs = blogs;
        }
    }

    @JsonIgnore
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

    @JsonIgnore
    public List<Category> getCategories() {
        return categories == null ? null : new ArrayList<>(this.categories);
    }

    public void setCategories(List<Category> categories) {
        if (categories == null) {
            this.categories = null;
        } else {
            this.categories = categories;
        }
    }

    public Boolean isEnabled() {
        return this.enabled;
    }

    @JsonIgnore
    public List<Role> getRoles() {
        return roles == null ? null : new ArrayList<>(this.roles);
    }

    public void setRoles(List<Role> roles) {
        if(roles == null) {
            this.roles = null;
        }else {
            this.roles = roles;
        }

    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }


}

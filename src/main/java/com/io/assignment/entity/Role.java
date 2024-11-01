package com.io.assignment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.io.assignment.enums.RoleName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "roles")
public class Role {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(name = "name")
    private RoleName name;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns =  @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    @JsonIgnore
    public List<User> getUsers() {
        return users == null ? null : new ArrayList<>(this.users);
    }

    public void setUsers(List<User> users) {
        if(users == null) {
            this.users = null;
        }else {
            this.users = users;
        }

    }
}

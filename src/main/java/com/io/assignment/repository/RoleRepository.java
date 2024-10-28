package com.io.assignment.repository;

import com.io.assignment.entity.Role;
import com.io.assignment.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByName(RoleName admin);

}

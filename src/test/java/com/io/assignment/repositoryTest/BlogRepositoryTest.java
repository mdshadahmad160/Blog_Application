package com.io.assignment.repositoryTest;

import com.io.assignment.entity.Role;
import com.io.assignment.entity.User;
import com.io.assignment.enums.RoleName;
import com.io.assignment.repository.*;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author Shad Ahmad
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Rollback(value = false)
@RequiredArgsConstructor
class BlogRepositoryTest {

    private final BlogRepository blogRepository;

    private final CategoryRepository categoryRepository;

    private final TagRepository tagRepository;

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    @Test
    @Order(1)
    void testCreateUser() {
        User user = new User();
        user.setAddress("address");
        user.setBirthday(null);
        user.setEmail("shad@gmail.com");
        user.setComments(null);
        user.setBlogs(null);
        user.setCategories(null);
        user.setEnabled(true);
        user.setFirstName("shad");
        user.setLastName("Ahmad");
        user.setImage("image");
        user.setPhoneNumber("8789785887");
        user.setPassword(passwordEncoder.encode("123"));
        user.setUsername("shadahmad@#");

        Role role = new Role();
        role.setName(RoleName.ADMIN);
        roleRepository.save(role);

        user.setRoles(Arrays.asList(role));

        userRepository.save(user);
        User userSub = new User();
        userSub.setAddress("address sub");
        userSub.setBirthday(null);
        userSub.setEmail("emailSub@gmail.com");
        userSub.setComments(null);
        userSub.setBlogs(null);
        userSub.setCategories(null);
        userSub.setEnabled(true);
        userSub.setFirstName("First name sub");
        userSub.setLastName("Last name sub");
        userSub.setImage("Image sub");
        userSub.setPhoneNumber("Phone number sub");
        userSub.setPassword(passwordEncoder.encode("123"));
        userSub.setUsername("kiet");

        Role roleSub = new Role();
        roleSub.setName(RoleName.USER);
        roleRepository.save(roleSub);
        userSub.setRoles(Arrays.asList(roleSub));

        userRepository.save(userSub);

        // add user 3
        User userSub1 = new User();
        userSub1.setAddress("address sub1");
        userSub1.setBirthday(null);
        userSub1.setEmail("emailSub1@gmail.com");
        userSub1.setComments(null);
        userSub1.setBlogs(null);
        userSub1.setCategories(null);
        userSub1.setEnabled(true);
        userSub1.setFirstName("First name sub1");
        userSub1.setLastName("Last name sub1");
        userSub1.setImage("Image sub1");
        userSub1.setPhoneNumber("Phone number sub1");
        userSub1.setPassword(passwordEncoder.encode("123"));
        userSub1.setUsername("Guddu");

        Role roleSub1 = new Role();
        roleSub1.setName(RoleName.STUDENT);
        roleRepository.save(roleSub1);
        userSub1.setRoles(Arrays.asList(roleSub1));

        userRepository.save(userSub1);

    }


}

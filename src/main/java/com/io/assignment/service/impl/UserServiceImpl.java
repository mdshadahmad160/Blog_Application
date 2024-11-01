package com.io.assignment.service.impl;

import com.io.assignment.entity.Role;
import com.io.assignment.entity.User;
import com.io.assignment.enums.RoleName;
import com.io.assignment.exception.AccessDeniedException;
import com.io.assignment.exception.ResourceExistException;
import com.io.assignment.exception.ResourceNotFoundException;
import com.io.assignment.exception.UserNotFoundException;
import com.io.assignment.payload.request.UserRequest;
import com.io.assignment.payload.response.ApiResponse;
import com.io.assignment.payload.response.UserProfileResponse;
import com.io.assignment.payload.response.UserResponse;
import com.io.assignment.repository.BlogRepository;
import com.io.assignment.repository.RoleRepository;
import com.io.assignment.repository.UserRepository;
import com.io.assignment.security.UserPrincipal;
import com.io.assignment.service.UserService;
import com.io.assignment.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    private final BlogRepository blogRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;


    @Override
    public UserProfileResponse getCurrentUser(UserPrincipal userPrincipal) {
        String username = userPrincipal.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException(AppConstant.USER_NOT_FOUND + username));

        Integer blogsCount = blogRepository.countByUser(user);

        UserProfileResponse userProfile = new UserProfileResponse();
        userProfile.setId(user.getId());
        userProfile.setUsername(username);
        userProfile.setFirstName(user.getFirstName());
        userProfile.setLastName(user.getLastName());
        userProfile.setEmail(user.getEmail());
        userProfile.setPhoneNumber(user.getPhoneNumber());
        userProfile.setImage(user.getImage());
        userProfile.setAddress(user.getAddress());
        userProfile.setBirthday(user.getBirthday());
        userProfile.setBlogsCount(blogsCount);
        userProfile.setEnabled(user.getEnabled());
        return userProfile;

    }

    @Override
    public Boolean checkUsernameUnique(String username) {
        return !userRepository.existsByUsername(username);
    }

    @Override
    public Boolean checkEmailUnique(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Override
    public UserProfileResponse getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException(AppConstant.USER_NOT_FOUND + username));

        Integer blogsCount = blogRepository.countByUser(user);

        UserProfileResponse userProfile = new UserProfileResponse();
        userProfile.setId(user.getId());
        userProfile.setUsername(username);
        userProfile.setFirstName(user.getFirstName());
        userProfile.setLastName(user.getLastName());
        userProfile.setEmail(user.getEmail());
        userProfile.setPhoneNumber(user.getPhoneNumber());
        userProfile.setImage(user.getImage());
        userProfile.setAddress(user.getAddress());
        userProfile.setBirthday(user.getBirthday());
        userProfile.setBlogsCount(blogsCount);
        userProfile.setEnabled(user.getEnabled());

        return userProfile;
    }

    @Override
    public ApiResponse deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException(AppConstant.USER_NOT_FOUND + username));

        userRepository.delete(user);
        return new ApiResponse(Boolean.TRUE, AppConstant.USER_DELETE_MESSAGE, HttpStatus.OK);
    }

    @Override
    public UserResponse updateUserByUsername(String username, User userUpdate, UserPrincipal currentUser) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        AppConstant.USER_NOT_FOUND + username));
        if (user.getId().equals(currentUser.getId()) ||
                currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + RoleName.ADMIN.toString()))) {

            user.setUsername(userUpdate.getUsername());
            user.setBirthday(userUpdate.getBirthday());
            user.setFirstName(userUpdate.getFirstName());
            user.setLastName(userUpdate.getLastName());
            user.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
            user.setEmail(userUpdate.getEmail());
            user.setEnabled(userUpdate.getEnabled());
            user.setImage(userUpdate.getImage());
            user.setAddress(userUpdate.getAddress());
            user.setPhoneNumber(userUpdate.getPhoneNumber());

            userRepository.save(user);
            return modelMapper.map(user, UserResponse.class);
        }
        throw new AccessDeniedException(AppConstant.USER_UPDATE_DENY);

    }

    @Override
    public ApiResponse giveAdmin(String username) {


        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        AppConstant.USER_NOT_FOUND + username));
        for (Role r : user.getRoles()) {
            if (r.getName().equals(RoleName.ADMIN)) {
                throw new ResourceExistException(AppConstant.ROLE_WITH_USER_EXIST);
            }
        }
        Role role = roleRepository.findByName(RoleName.ADMIN);
        if (Objects.isNull(role)) {
            throw new ResourceNotFoundException(AppConstant.ROLE_NOT_FOUND + RoleName.ADMIN);
        }
        user.addRole(role);
        userRepository.save(user);

        return new ApiResponse(Boolean.TRUE, AppConstant.GIVE_ADMIN, HttpStatus.OK);
    }

    @Override
    public ApiResponse takeAdmin(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        AppConstant.USER_NOT_FOUND + username));

        List<Role> roles = user.getRoles().stream()
                .filter(role -> role.getName().equals(RoleName.ADMIN))
                .collect(Collectors.toList());

        if (roles.isEmpty()) {
            throw new ResourceNotFoundException(
                    AppConstant.ROLE_NOT_FOUND_WITH_USER + RoleName.ADMIN);
        }

        user.removeRole(roles.get(0));

        userRepository.save(user);

        return new ApiResponse(Boolean.TRUE, AppConstant.TAKE_ADMIN, HttpStatus.OK);
    }

    @Override
    public UserProfileResponse createUser(UserRequest userRequest) {

        String username = userRequest.getUsername();
        if (userRepository.existsByUsername(username)) {
            throw new ResourceExistException(AppConstant.USER_EXIST);
        }
        Role role = roleRepository.findByName(RoleName.USER);
        if (Objects.isNull(role)) {
            throw new ResourceNotFoundException(AppConstant.ROLE_NOT_FOUND + RoleName.USER);
        }

        User user = modelMapper.map(userRequest, User.class);
        user.setRoles(Arrays.asList(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return modelMapper.map(user, UserProfileResponse.class);
    }
}

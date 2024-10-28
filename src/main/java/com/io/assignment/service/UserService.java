package com.io.assignment.service;

import com.io.assignment.entity.User;
import com.io.assignment.payload.request.UserRequest;
import com.io.assignment.payload.response.ApiResponse;
import com.io.assignment.payload.response.UserProfileResponse;
import com.io.assignment.payload.response.UserResponse;
import com.io.assignment.security.UserPrincipal;

public interface UserService {


    UserProfileResponse getCurrentUser(com.io.assignment.security.UserPrincipal userPrincipal);

    Boolean checkUsernameUnique(String username);

    Boolean checkEmailUnique(String email);

    UserProfileResponse getUserProfile(String username);

    ApiResponse deleteUserByUsername(String username);

    UserResponse updateUserByUsername(String username, User userUpdate, UserPrincipal currentUser);

    ApiResponse giveAdmin(String username);

    ApiResponse takeAdmin(String username);

    UserProfileResponse createUser(UserRequest userRequest);


}

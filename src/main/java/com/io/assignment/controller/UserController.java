package com.io.assignment.controller;

import com.io.assignment.payload.request.UserRequest;
import com.io.assignment.payload.response.UserProfileResponse;
import com.io.assignment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<UserProfileResponse> registerUserHandler(@RequestBody UserRequest userRequest) {
        UserProfileResponse profileResponse = userService.createUser(userRequest);
        return new ResponseEntity<>(profileResponse, HttpStatus.CREATED);
    }

    @GetMapping("/unique-username")
    public ResponseEntity<Boolean> checkUsernameUnique(@RequestParam("username") String username) {
        Boolean unique = userService.checkUsernameUnique(username);

        return new ResponseEntity<>(unique, HttpStatus.OK);

    }
    @GetMapping("/unique-email")
    public ResponseEntity<Boolean> checkEmailUnique(@RequestParam("email") String email){
        Boolean unique=userService.checkEmailUnique(email);
        return new ResponseEntity<>(unique, HttpStatus.OK);

    }
}

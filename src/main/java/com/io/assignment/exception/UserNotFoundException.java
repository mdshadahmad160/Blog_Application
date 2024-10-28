package com.io.assignment.exception;

public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -7481814820357291911L;

    public UserNotFoundException(String message) {
        super(message);
    }
}

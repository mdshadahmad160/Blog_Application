package com.blogapp.shad.exception;

import org.springframework.http.HttpStatus;

public class BlogApPIException extends RuntimeException {

    private HttpStatus status;
    private String message;



    public BlogApPIException(HttpStatus status,String message){
        this.status=status;
        this.message=message;
    }
    public BlogApPIException(String message,HttpStatus status, String message1){
        super(message);
        this.status=status;
        this.message=message1;
    }


    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

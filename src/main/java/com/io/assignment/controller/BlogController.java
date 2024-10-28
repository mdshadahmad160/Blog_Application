package com.io.assignment.controller;

import com.io.assignment.payload.request.CommentRequest;
import com.io.assignment.payload.response.CommentResponse;
import com.io.assignment.security.CurrentUser;
import com.io.assignment.security.UserPrincipal;
import com.io.assignment.service.BlogService;
import com.io.assignment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    private final CommentService commentService;

    @PostMapping("/{blog_id}/comments")
    public ResponseEntity<CommentResponse> addComment(@PathVariable("blog_id") Long blogId,
                                                      @RequestBody CommentRequest commentRequest,
                                                      @CurrentUser UserPrincipal userPrincipal) {
        CommentResponse commentResponse = commentService.addComment(blogId, commentRequest, userPrincipal);
        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }

}

package com.io.assignment.controller;

import com.io.assignment.payload.request.BlogRequest;
import com.io.assignment.payload.request.CommentRequest;
import com.io.assignment.payload.response.BlogResponse;
import com.io.assignment.payload.response.CommentResponse;
import com.io.assignment.payload.response.PageResponse;
import com.io.assignment.security.CurrentUser;
import com.io.assignment.security.UserPrincipal;
import com.io.assignment.service.BlogService;
import com.io.assignment.service.CommentService;
import com.io.assignment.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Shad Ahmad
 * @apiNote this could be the Blog Controller
 */
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

    @PostMapping
    public ResponseEntity<BlogResponse> addBlog(@RequestBody BlogRequest blogRequest,
                                                @CurrentUser UserPrincipal userPrincipal) {
        BlogResponse blogResponse = blogService.addBlog(blogRequest, userPrincipal);
        return new ResponseEntity<>(blogResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PageResponse<BlogResponse>> getAllBlogs(
            @RequestParam(value = "page", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer size
    ) {
        PageResponse<BlogResponse> pageResponse = blogService.getAllBlogs(page, size);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/{blog_id}")
    public ResponseEntity<BlogResponse> getBlogById(@PathVariable("blog_id") Long blogId) {
        BlogResponse blogResponse = blogService.getBlogsById(blogId);
        return new ResponseEntity<>(blogResponse, HttpStatus.OK);
    }

    @GetMapping("/{blog_id}/comments")
    public ResponseEntity<PageResponse<CommentResponse>> getCommentsByBlog(
            @PathVariable("blog_id") Long blogId,
            @RequestParam(value = "page", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer size
    ) {
        PageResponse<CommentResponse> pageResponse = commentService.getCommentByBlog(blogId, page, size);

        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }


}

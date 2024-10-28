package com.io.assignment.controller;

import com.io.assignment.payload.request.BlogRequest;
import com.io.assignment.payload.request.CommentRequest;
import com.io.assignment.payload.response.ApiResponse;
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
 * @apiNote this could be the Blog Controller but its depends on Tag and Comment Entity also
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

    @DeleteMapping("/{blog_id}")
    public ResponseEntity<ApiResponse> deleteBlogById(@PathVariable("blog_id") Long blogId,
                                                      @CurrentUser UserPrincipal userPrincipal) {
        ApiResponse apiResponse = blogService.deleteBlogById(blogId, userPrincipal);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{blog_id}/comments")
    public ResponseEntity<ApiResponse> deleteCommentsByBlog(@PathVariable("blog_id") Long blogId,
                                                            @CurrentUser UserPrincipal userPrincipal) {
        ApiResponse apiResponse = commentService.deleteCommentsByBlog(blogId, userPrincipal);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{blog_id}")
    public ResponseEntity<BlogResponse> updateBlogById(@PathVariable("blog_id") Long blogId,
                                                       @RequestBody BlogRequest blogRequest,
                                                       @CurrentUser UserPrincipal userPrincipal) {
        BlogResponse blogResponse = blogService.updateBlogById(blogId, blogRequest, userPrincipal);
        return new ResponseEntity<>(blogResponse, HttpStatus.OK);
    }


}

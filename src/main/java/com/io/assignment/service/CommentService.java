package com.io.assignment.service;

import com.io.assignment.payload.request.CommentRequest;
import com.io.assignment.payload.response.ApiResponse;
import com.io.assignment.payload.response.CommentResponse;
import com.io.assignment.payload.response.PageResponse;
import com.io.assignment.security.UserPrincipal;

public interface CommentService {
    PageResponse<CommentResponse> getAllComments(Integer page, Integer size);

    CommentResponse getCommentById(Long commentId);

    PageResponse<CommentResponse> getCommentByBlog(Long blogId, Integer page, Integer size);

    ApiResponse deleteById(Long commentId, UserPrincipal userPrincipal);

    ApiResponse deleteCommentsByBlog(Long blogId, UserPrincipal userPrincipal);

    CommentResponse updateCommentById(Long commentId, CommentRequest commentRequest, UserPrincipal userPrincipal);

    PageResponse<CommentResponse> getCommentsByUsername(String username, Integer page, Integer size);

    CommentResponse addComment(Long blogId, CommentRequest commentRequest, UserPrincipal userPrincipal);


}


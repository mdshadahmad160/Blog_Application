package com.io.assignment.service;

import com.io.assignment.payload.request.BlogRequest;
import com.io.assignment.payload.response.ApiResponse;
import com.io.assignment.payload.response.BlogResponse;
import com.io.assignment.payload.response.PageResponse;
import com.io.assignment.security.UserPrincipal;

public interface BlogService {

    PageResponse<BlogResponse> getAllBlogs(Integer page, Integer size);

    BlogResponse getBlogsById(Long blogId);

    PageResponse<BlogResponse> getBlogsByCategory(Long categoryId, Integer page, Integer size);

    PageResponse<BlogResponse> getBlogsByTag(Long tagId, Integer page, Integer size);

    BlogResponse addBlog(BlogRequest blogRequest, UserPrincipal userPrincipal);

    ApiResponse deleteBlogById(Long blogId, UserPrincipal userPrincipal);

    ApiResponse deleteBlogsByCategory(Long categoryId, UserPrincipal userPrincipal);

    BlogResponse updateBlogById(Long blogId, BlogRequest blogRequest, UserPrincipal userPrincipal);

    PageResponse<BlogResponse> getBlogsByUsername(String username, Integer page, Integer size);


}

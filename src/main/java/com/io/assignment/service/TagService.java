package com.io.assignment.service;

import com.io.assignment.payload.request.TagRequest;
import com.io.assignment.payload.response.ApiResponse;
import com.io.assignment.payload.response.PageResponse;
import com.io.assignment.payload.response.TagResponse;
import com.io.assignment.security.UserPrincipal;

/**
 * @author Shad Agmad
 */
public interface TagService {

    TagResponse addTag(TagRequest tagRequest);

    TagResponse getTagById(Long tagId);

    PageResponse<TagResponse> getTagsByBlog(Long blogId, Integer page, Integer size);

    PageResponse<TagResponse> getAllTags(Integer page, Integer size);

    ApiResponse deleteTagById(Long tagId);

    ApiResponse deleteAllTag();

    ApiResponse removeTagsByBlog(Long blogId, UserPrincipal userPrincipal);

    TagResponse updateTagById(Long tagId, TagRequest tagRequest);

}

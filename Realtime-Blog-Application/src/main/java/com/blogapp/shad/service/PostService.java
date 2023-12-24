package com.blogapp.shad.service;

import com.blogapp.shad.payload.PostDto;
import com.blogapp.shad.payload.PostResponse;

import java.util.List;

/**
 * @author  Shad Ahmad
 */

public interface PostService {
    /**
     *
     * @param postDto
     * @return
     */


    PostDto createPost(PostDto postDto);

    /**
     *
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */

    PostResponse getAllPosts(int pageNo,int pageSize, String sortBy,String sortDir);

    /**
     *
     * @param id
     * @return
     */
    PostDto getPostById(Long id);

    /**
     *
     * @param postDto
     * @param id
     * @return
     */
    PostDto updatePost(PostDto postDto,Long id);

    /**
     *
     * @param id
     */
    void deletePostById(Long id);

    /**
     *
     * @param categoryId
     * @return
     */

    List<PostDto> getPostsByCategory(Long categoryId);

    /**
     *
     * @param query
     * @return
     */

    List<PostDto> searchProducts(String query);

}

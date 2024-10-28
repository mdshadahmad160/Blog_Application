package com.io.assignment.service;

import com.io.assignment.payload.request.CategoryRequest;
import com.io.assignment.payload.response.ApiResponse;
import com.io.assignment.payload.response.CategoryResponse;
import com.io.assignment.payload.response.PageResponse;
import com.io.assignment.security.UserPrincipal;

public interface CategoryService {


    PageResponse<CategoryResponse> getAllCategories(Integer page, Integer size);

    CategoryResponse getCategoryById(Long categoryId);

    CategoryResponse createCategory(CategoryRequest categoryRequest, UserPrincipal userPrincipal);

    ApiResponse deleteCategoryById(Long categoryId, UserPrincipal userPrincipal);

    ApiResponse deleteAll();

    CategoryResponse updateCategoryById(Long categoryId, CategoryRequest categoryRequest, UserPrincipal userPrincipal);


    PageResponse<CategoryResponse> getCategoriesByUsername(String username, Integer page, Integer size);

}

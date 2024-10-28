package com.io.assignment.controller;

import com.io.assignment.payload.request.CategoryRequest;
import com.io.assignment.payload.response.ApiResponse;
import com.io.assignment.payload.response.BlogResponse;
import com.io.assignment.payload.response.CategoryResponse;
import com.io.assignment.payload.response.PageResponse;
import com.io.assignment.security.CurrentUser;
import com.io.assignment.security.UserPrincipal;
import com.io.assignment.service.BlogService;
import com.io.assignment.service.CategoryService;
import com.io.assignment.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Shad Ahmad
 */
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {


    private final CategoryService categoryService;

    private final BlogService blogService;

    @GetMapping
    public ResponseEntity<PageResponse<CategoryResponse>> getAllCategories(
            @RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer size
    ) {
        PageResponse<CategoryResponse> categoryResponse = categoryService.getAllCategories(page, size);

        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @GetMapping("/{category_id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable("category_id") Long categoryId) {
        CategoryResponse categoryResponse = categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @GetMapping("/{category_id}/blogs")
    public ResponseEntity<PageResponse<BlogResponse>> getBlogsByCategory(
            @PathVariable("category_id") Long categoryId,
            @RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer size
    ) {
        PageResponse<BlogResponse> pageResponse = blogService.getBlogsByCategory(categoryId, page, size);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest,
                                                           @CurrentUser UserPrincipal userPrincipal) {
        CategoryResponse categoryResponse = categoryService.createCategory(categoryRequest, userPrincipal);
        return new ResponseEntity<>(categoryResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{category_id}")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable("category_id") Long categoryId,
                                                          @CurrentUser UserPrincipal userPrincipal) {
        ApiResponse apiResponse = categoryService.deleteCategoryById(categoryId, userPrincipal);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteAll() {
        ApiResponse apiResponse = categoryService.deleteAll();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    @DeleteMapping("/{category_id}/blogs")
    public ResponseEntity<ApiResponse> deleteBlogsByCategory(@PathVariable("category_id") Long categoryId,
                                                             @CurrentUser UserPrincipal userPrincipal) {
        ApiResponse apiResponse = blogService.deleteBlogsByCategory(categoryId, userPrincipal);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    @PutMapping("/{category_id}")
    public ResponseEntity<CategoryResponse> updateCategoryById(@PathVariable("category_id") Long categoryId,
                                                               @RequestBody CategoryRequest categoryRequest,
                                                               @CurrentUser UserPrincipal userPrincipal) {
        CategoryResponse categoryResponse = categoryService.updateCategoryById(categoryId, categoryRequest, userPrincipal);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

}

package com.io.assignment.controller;

import com.io.assignment.payload.response.CategoryResponse;
import com.io.assignment.payload.response.PageResponse;
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

        return new ResponseEntity<>(categoryResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{category_id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable("category_id") Long categoryId) {
        CategoryResponse categoryResponse = categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }


}

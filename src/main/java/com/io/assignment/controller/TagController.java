package com.io.assignment.controller;

import com.io.assignment.payload.request.TagRequest;
import com.io.assignment.payload.response.ApiResponse;
import com.io.assignment.payload.response.BlogResponse;
import com.io.assignment.payload.response.PageResponse;
import com.io.assignment.payload.response.TagResponse;
import com.io.assignment.service.BlogService;
import com.io.assignment.service.TagService;
import com.io.assignment.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Shad Ahmad
 * @since 30-10-2024
 */
@RestController
@RequestMapping("api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    private final BlogService blogService;

    @GetMapping
    public ResponseEntity<PageResponse<TagResponse>> getAllTags(
            @RequestParam(value = "page", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer size
    ) {
        PageResponse<TagResponse> pageResponse = tagService.getAllTags(page, size);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/{tag_id}")
    public ResponseEntity<TagResponse> getTagById(@PathVariable("tag_id") Long tagId) {
        TagResponse tagResponse = tagService.getTagById(tagId);
        return new ResponseEntity<>(tagResponse, HttpStatus.OK);
    }

    @GetMapping("/{tag_id}/blogs")
    public ResponseEntity<PageResponse<BlogResponse>> getBlogsByTag(
            @PathVariable("tag_id") Long tagId,
            @RequestParam(value = "page", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer size
    ) {
        PageResponse<BlogResponse> blogResponse = blogService.getBlogsByTag(tagId, page, size);
        return new ResponseEntity<>(blogResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TagResponse> addTag(@RequestBody TagRequest tagRequest) {
        TagResponse tagResponse = tagService.addTag(tagRequest);
        return new ResponseEntity<>(tagResponse, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteAllTag() {
        ApiResponse apiResponse = tagService.deleteAllTag();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{tag_id}")
    public ResponseEntity<ApiResponse> deleteTagById(@PathVariable("tag_id") Long tagId) {
        ApiResponse apiResponse = tagService.deleteTagById(tagId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{tag_id}")
    public ResponseEntity<TagResponse> updateTagById(@PathVariable("tag_id") Long tagId,
                                                     @RequestBody TagRequest tagRequest) {
        TagResponse tagResponse = tagService.updateTagById(tagId, tagRequest);
        return new ResponseEntity<>(tagResponse, HttpStatus.OK);
    }

}

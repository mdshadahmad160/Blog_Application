package com.io.assignment.service.impl;

import com.io.assignment.entity.Category;
import com.io.assignment.entity.User;
import com.io.assignment.enums.RoleName;
import com.io.assignment.exception.AccessDeniedException;
import com.io.assignment.exception.ResourceExistException;
import com.io.assignment.exception.ResourceNotFoundException;
import com.io.assignment.exception.UserNotFoundException;
import com.io.assignment.payload.request.CategoryRequest;
import com.io.assignment.payload.response.ApiResponse;
import com.io.assignment.payload.response.CategoryResponse;
import com.io.assignment.payload.response.PageResponse;
import com.io.assignment.repository.CategoryRepository;
import com.io.assignment.repository.UserRepository;
import com.io.assignment.security.UserPrincipal;
import com.io.assignment.service.CategoryService;
import com.io.assignment.utils.AppConstant;
import com.io.assignment.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;


    private final UserRepository userRepository;


    private final ModelMapper modelMapper;

    @Override
    public PageResponse<CategoryResponse> getAllCategories(Integer page, Integer size) {
        AppUtils.validatePageAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categories = categoryRepository.findAll(pageable);
        List<CategoryResponse> categoryResponses = Arrays.asList(modelMapper.map(categories.getContent(), CategoryResponse[].class));

        PageResponse<CategoryResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(categoryResponses);
        pageResponse.setPage(page);
        pageResponse.setSize(size);
        pageResponse.setTotalPages(categories.getTotalPages());
        pageResponse.setTotalElements(categories.getTotalElements());
        pageResponse.setLast(categories.isLast());
        return pageResponse;
    }

    @Override
    public CategoryResponse getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND + categoryId));
        return modelMapper.map(category, CategoryResponse.class);
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest, UserPrincipal userPrincipal) {

        Category category = modelMapper.map(categoryRequest, Category.class);
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new ResourceExistException(AppConstant.CATEGORY_EXIST);
        }
        User user = modelMapper.map(userPrincipal, User.class);
        category.setUser(user);
        categoryRepository.save(category);
        return modelMapper.map(category, CategoryResponse.class);
    }

    @Override
    public ApiResponse deleteCategoryById(Long categoryId, UserPrincipal userPrincipal) throws AccessDeniedException {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND + categoryId));

        User user = userRepository.findByCategories(category);
        if (user.getId().equals(userPrincipal.getId()) || userPrincipal.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + RoleName.ADMIN.toString()))) {
            categoryRepository.delete(category);
            return new ApiResponse(Boolean.TRUE, AppConstant.CATEGORY_DELETE_MESSAGE, HttpStatus.OK);
        }
        throw new AccessDeniedException(AppConstant.CATEGORY_DELETE_DENY);
    }

    @Override
    public ApiResponse deleteAll() {
        categoryRepository.deleteAll();

        return new ApiResponse(Boolean.TRUE, AppConstant.CATEGORY_DELETE_MESSAGE, HttpStatus.OK);
    }

    @Override
    public CategoryResponse updateCategoryById(Long categoryId, CategoryRequest categoryRequest, UserPrincipal userPrincipal) {
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new ResourceExistException(AppConstant.CATEGORY_EXIST);
        }

        modelMapper.typeMap(CategoryRequest.class, Category.class).addMappings(mapper -> mapper.skip(Category::setId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND + categoryId));

        User user = userRepository.findByCategories(category);
        if (user.getId().equals(userPrincipal.getId()) || userPrincipal.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_" + RoleName.ADMIN.toString()))) {

            modelMapper.map(categoryRequest, category);

            categoryRepository.save(category);

            return modelMapper.map(category, CategoryResponse.class);
        }

        throw new AccessDeniedException(AppConstant.CATEGORY_UPDATE_DENY);
    }

    @Override
    public PageResponse<CategoryResponse> getCategoriesByUsername(String username, Integer page, Integer size) {
        AppUtils.validatePageAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(AppConstant.USER_NOT_FOUND + username));
        Page<Category> categories = categoryRepository.findByUser(user, pageable);
        List<CategoryResponse> categoryResponses = Arrays
                .asList(modelMapper.map(categories.getContent(), CategoryResponse[].class));
        PageResponse<CategoryResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(categoryResponses);
        pageResponse.setPage(page);
        pageResponse.setSize(size);
        pageResponse.setTotalElements(categories.getTotalElements());
        pageResponse.setTotalPages(categories.getTotalPages());
        pageResponse.setLast(categories.isLast());

        return pageResponse;
    }
}


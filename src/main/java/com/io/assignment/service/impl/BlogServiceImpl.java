package com.io.assignment.service.impl;

import com.io.assignment.entity.*;
import com.io.assignment.enums.RoleName;
import com.io.assignment.exception.AccessDeniedException;
import com.io.assignment.exception.ResourceExistException;
import com.io.assignment.exception.ResourceNotFoundException;
import com.io.assignment.exception.UserNotFoundException;
import com.io.assignment.payload.request.BlogRequest;
import com.io.assignment.payload.response.ApiResponse;
import com.io.assignment.payload.response.BlogResponse;
import com.io.assignment.payload.response.PageResponse;
import com.io.assignment.repository.BlogRepository;
import com.io.assignment.repository.CategoryRepository;
import com.io.assignment.repository.TagRepository;
import com.io.assignment.repository.UserRepository;
import com.io.assignment.security.UserPrincipal;
import com.io.assignment.service.BlogService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Shad Ahmad
 */
@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {


    private final BlogRepository blogRepository;

    private final CategoryRepository categoryRepository;

    private final TagRepository tagRepository;

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

    @Override
    public PageResponse<BlogResponse> getAllBlogs(Integer page, Integer size) {
        /**
         * We will check validating by factory method
         */
        AppUtils.validatePageAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Blog> blogs = blogRepository.findAll(pageable);

        List<BlogResponse> blogResponses = Arrays.asList(
                modelMapper.map(blogs.getContent(), BlogResponse[].class));

        PageResponse<BlogResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(blogResponses);
        pageResponse.setSize(size);
        pageResponse.setPage(page);
        pageResponse.setTotalElements(blogs.getNumberOfElements());
        pageResponse.setTotalPages(blogs.getTotalPages());
        pageResponse.setLast(blogs.isLast());
        return pageResponse;
    }

    @Override
    public BlogResponse getBlogsById(Long blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(AppConstant.BLOG_NOT_FOUND + blogId)
                );

        return modelMapper.map(blog, BlogResponse.class);
    }

    @Override
    public PageResponse<BlogResponse> getBlogsByCategory(Long categoryId, Integer page, Integer size) {
        AppUtils.validatePageAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND + categoryId)
                );
        Page<Blog> blogs = blogRepository.findByCategory(category, pageable);
        List<BlogResponse> blogResponses = Arrays.asList(
                modelMapper.map(blogs.getContent(), BlogResponse[].class)
        );
        PageResponse<BlogResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(blogResponses);
        pageResponse.setSize(size);
        pageResponse.setPage(page);
        pageResponse.setTotalElements(blogs.getNumberOfElements());
        pageResponse.setTotalPages(blogs.getTotalPages());
        pageResponse.setLast(blogs.isLast());
        return pageResponse;
    }

    @Override
    public PageResponse<BlogResponse> getBlogsByTag(Long tagId, Integer page, Integer size) {
        AppUtils.validatePageAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size);
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(AppConstant.TAG_NOT_FOUND + tagId)
                );
        Page<Blog> blogs = blogRepository.findByTags(tag, pageable);
        List<BlogResponse> blogResponses = Arrays.asList(
                modelMapper.map(blogs.getContent(), BlogResponse[].class)
        );
        PageResponse<BlogResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(blogResponses);
        pageResponse.setSize(size);
        pageResponse.setPage(page);
        pageResponse.setTotalElements(blogs.getNumberOfElements());
        pageResponse.setTotalPages(blogs.getTotalPages());
        pageResponse.setLast(blogs.isLast());
        return pageResponse;
    }

    @Override
    public BlogResponse addBlog(BlogRequest blogRequest, UserPrincipal userPrincipal) {
        // skip id so that model mapping not map categoryId with blogId

        modelMapper.typeMap(BlogRequest.class, Blog.class).addMappings(mapper -> mapper.skip(Blog::setId));

        Blog blog = modelMapper.map(blogRequest, Blog.class);
        if (blogRepository.findByTitle(blog.getTitle()).isPresent()) {
            throw new ResourceExistException(AppConstant.BLOG_EXIST);
        }
        Long id = blog.getCategory().getId();
        Category category = categoryRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND + id)
                );
        User user = modelMapper.map(userPrincipal, User.class);

        List<Tag> tags = new ArrayList<>();

        for (Tag tag : blog.getTags()) {
            if (!tagRepository.existsByName(tag.getName())) {
                tags.add(tagRepository.save(tag));
            } else {
                tags.add(tagRepository.findByName(tag.getName()).get());
            }

        }
        for (Comment comment : blog.getComments()) {
            comment.setBlog(blog);
            comment.setUser(user);
        }
        blog.setTags(tags);
        blog.setUser(user);
        System.out.println(blog.getId());
        blogRepository.save(blog);
        return modelMapper.map(blog, BlogResponse.class);
    }

    @Override
    public ApiResponse deleteBlogById(Long blogId, UserPrincipal userPrincipal) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.BLOG_NOT_FOUND + blogId));

        User user = blog.getUser();
        if (user.getId().equals(userPrincipal.getId()) || userPrincipal.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_" + RoleName.ADMIN.toString()))) {
            blogRepository.delete(blog);
            return new ApiResponse(Boolean.TRUE, AppConstant.BLOG_DELETE_MESSAGE, HttpStatus.OK);
        }
        throw new AccessDeniedException(AppConstant.BLOG_DELETE_DENY);

    }

    @Override
    public ApiResponse deleteBlogsByCategory(Long categoryId, UserPrincipal userPrincipal) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND + categoryId));

        User user = userRepository.findByCategories(category);
        if (user.getId().equals(userPrincipal.getId())
                || userPrincipal.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + RoleName.ADMIN.toString()))) {

            blogRepository.deleteAllByCategory(category);
            return new ApiResponse(Boolean.TRUE, AppConstant.BLOG_DELETE_MESSAGE, HttpStatus.OK);

        }

        throw new AccessDeniedException(AppConstant.BLOG_DELETE_DENY);
    }

    @Override
    public BlogResponse updateBlogById(Long blogId, BlogRequest blogRequest, UserPrincipal userPrincipal) {
        if (blogRepository.findByTitle(blogRequest.getTitle()).isPresent()) {
            throw new ResourceExistException(AppConstant.BLOG_EXIST);
        }
        modelMapper.typeMap(BlogRequest.class, Blog.class).addMappings(mapper -> mapper.skip(Blog::setId));

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(AppConstant.BLOG_NOT_FOUND + blogId)
                );
        User user = userRepository.findByBlogs(blog);
        if (user.getId().equals(userPrincipal.getId()) || userPrincipal.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_" + RoleName.ADMIN.toString()))) {

            Long categoryId = blogRequest.getCategoryId();
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(
                            () -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND + categoryId)
                    );
            blog.setBody(blogRequest.getBody());
            blog.setDescription(blogRequest.getDescription());
            blog.setImage(blogRequest.getImage());
            blog.setPublished(blogRequest.getPublished());
            blog.setTitle(blogRequest.getTitle());
            blog.setViews(blogRequest.getViews());
            blog.setCategory(category);

            blogRepository.save(blog);
            return modelMapper.map(blog, BlogResponse.class);
        }
        throw new AccessDeniedException(AppConstant.BLOG_UPDATE_DENY);

    }


    @Override
    public PageResponse<BlogResponse> getBlogsByUsername(String username, Integer page, Integer size) {
        AppUtils.validatePageAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(AppConstant.USER_NOT_FOUND + username));

        Page<Blog> blogs = blogRepository.findByUser(user, pageable);
        List<BlogResponse> blogResponses = Arrays.asList(modelMapper.map(blogs.getContent(), BlogResponse[].class));

        PageResponse<BlogResponse> pageResponse = new PageResponse<>();
        pageResponse.setSize(size);
        pageResponse.setPage(page);
        pageResponse.setTotalElements(blogs.getTotalElements());
        pageResponse.setTotalPages(blogs.getTotalPages());
        pageResponse.setLast(blogs.isLast());
        pageResponse.setContent(blogResponses);

        return pageResponse;
    }
}


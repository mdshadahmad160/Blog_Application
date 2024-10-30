package com.io.assignment.service.impl;

import com.io.assignment.entity.Blog;
import com.io.assignment.entity.Tag;
import com.io.assignment.entity.User;
import com.io.assignment.enums.RoleName;
import com.io.assignment.exception.AccessDeniedException;
import com.io.assignment.exception.ResourceExistException;
import com.io.assignment.exception.ResourceNotFoundException;
import com.io.assignment.payload.request.TagRequest;
import com.io.assignment.payload.response.ApiResponse;
import com.io.assignment.payload.response.PageResponse;
import com.io.assignment.payload.response.TagResponse;
import com.io.assignment.repository.BlogRepository;
import com.io.assignment.repository.TagRepository;
import com.io.assignment.repository.UserRepository;
import com.io.assignment.security.UserPrincipal;
import com.io.assignment.service.TagService;
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
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private final BlogRepository blogRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Override
    public TagResponse addTag(TagRequest tagRequest) {
        /**
         * Here we are trying to convert entity into dto
         */
        Tag tag = modelMapper.map(tagRequest, Tag.class);

        if (tagRepository.findByName(tag.getName()).isPresent()) {
            throw new ResourceExistException(AppConstant.TAG_EXIST);
        }
        tagRepository.save(tag);

        return modelMapper.map(tag, TagResponse.class);
    }

    @Override
    public TagResponse getTagById(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(AppConstant.TAG_NOT_FOUND + tagId)
                );

        return modelMapper.map(tag, TagResponse.class);
    }

    @Override
    public PageResponse<TagResponse> getTagsByBlog(Long blogId, Integer page, Integer size) {
        AppUtils.validatePageAndSize(page, size);

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(AppConstant.BLOG_NOT_FOUND + blogId)
                );
        Pageable pageable = PageRequest.of(page, size);
        Page<Tag> tags = tagRepository.findByBlogs(blog, pageable);

        List<TagResponse> tagResponses = Arrays.asList(
                modelMapper.map(tags.getContent(), TagResponse[].class)
        );

        PageResponse<TagResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(tagResponses);
        pageResponse.setPage(page);
        pageResponse.setSize(size);
        pageResponse.setTotalElements(tags.getNumberOfElements());
        pageResponse.setTotalPages(tags.getTotalPages());
        pageResponse.setLast(tags.isLast());

        return pageResponse;
    }

    @Override
    public PageResponse<TagResponse> getAllTags(Integer page, Integer size) {
        AppUtils.validatePageAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Tag> tags = tagRepository.findAll(pageable);
        List<TagResponse> tagResponses = Arrays.asList(
                modelMapper.map(tags.get(), TagResponse[].class)
        );

        PageResponse<TagResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(tagResponses);
        pageResponse.setSize(size);
        pageResponse.setPage(page);
        pageResponse.setTotalElements(tags.getNumberOfElements());
        pageResponse.setTotalPages(tags.getTotalPages());
        pageResponse.setLast(tags.isLast());
        return pageResponse;
    }

    @Override
    public ApiResponse deleteTagById(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(AppConstant.TAG_NOT_FOUND + tagId)
                );
        tagRepository.delete(tag);
        return new ApiResponse(Boolean.TRUE, AppConstant.TAG_DELETE_MESSAGE, HttpStatus.OK);
    }

    @Override
    public ApiResponse deleteAllTag() {
        tagRepository.findAll();
        return new ApiResponse(Boolean.TRUE, AppConstant.TAG_DELETE_MESSAGE, HttpStatus.OK);
    }

    @Override
    public ApiResponse removeTagsByBlog(Long blogId, UserPrincipal userPrincipal) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(AppConstant.BLOG_NOT_FOUND + blogId)
                );
        User user = userRepository.findByBlogs(blog);
        if (user.getId().equals(userPrincipal.getId()) || userPrincipal.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_" + RoleName.ADMIN))) {
            List<Tag> tags = tagRepository.findAllByBlogs(blog);

            for (Tag tag : tags) {
                tag.removeBlog(blog);

            }
            tagRepository.saveAll(tags);
            return new ApiResponse(Boolean.TRUE, AppConstant.TAG_DELETE_MESSAGE, HttpStatus.OK);
        }
        throw new AccessDeniedException(AppConstant.TAG_REMOVE_DENY);
    }

    @Override
    public TagResponse updateTagById(Long tagId, TagRequest tagRequest) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(AppConstant.TAG_NOT_FOUND + tagId)
                );
        modelMapper.map(tagRequest, tag);
        tagRepository.save(tag);
        return modelMapper.map(tag, TagResponse.class);
    }
}

package com.io.assignment.service.impl;

import com.io.assignment.entity.Blog;
import com.io.assignment.entity.Comment;
import com.io.assignment.entity.User;
import com.io.assignment.enums.RoleName;
import com.io.assignment.exception.AccessDeniedException;
import com.io.assignment.exception.ResourceNotFoundException;
import com.io.assignment.payload.request.CommentRequest;
import com.io.assignment.payload.response.ApiResponse;
import com.io.assignment.payload.response.CommentResponse;
import com.io.assignment.payload.response.PageResponse;
import com.io.assignment.repository.BlogRepository;
import com.io.assignment.repository.CommentRepository;
import com.io.assignment.repository.UserRepository;
import com.io.assignment.security.UserPrincipal;
import com.io.assignment.service.CommentService;
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

/**
 * @author Shad Ahmad
 */

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BlogRepository blogRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Override
    public PageResponse<CommentResponse> getAllComments(Integer page, Integer size) {
        AppUtils.validatePageAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentRepository.findAll(pageable);
        List<CommentResponse> commentResponses = Arrays.asList(
                modelMapper.map(comments.getContent(), CommentResponse[].class)
        );
        PageResponse<CommentResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(commentResponses);
        pageResponse.setPage(page);
        pageResponse.setSize(size);
        pageResponse.setTotalElements(comments.getNumberOfElements());
        pageResponse.setTotalPages(comments.getTotalPages());
        pageResponse.setLast(comments.isLast());
        return pageResponse;
    }

    @Override
    public CommentResponse getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(AppConstant.COMMENT_NOT_FOUND + commentId)
                );

        return modelMapper.map(comment, CommentResponse.class);
    }

    @Override
    public PageResponse<CommentResponse> getCommentByBlog(Long blogId, Integer page, Integer size) {
        AppUtils.validatePageAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size);
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(AppConstant.BLOG_NOT_FOUND + blogId)
                );
        Page<Comment> comments = commentRepository.findByBlog(blog, pageable);

        List<CommentResponse> commentResponses = Arrays.asList(
                modelMapper.map(comments.getContent(), CommentResponse[].class)
        );
        PageResponse<CommentResponse> pageResponse = new PageResponse<>();
        pageResponse.setContent(commentResponses);
        pageResponse.setPage(page);
        pageResponse.setSize(size);
        pageResponse.setTotalElements(comments.getNumberOfElements());
        pageResponse.setTotalPages(comments.getTotalPages());
        pageResponse.setLast(comments.isLast());
        return pageResponse;
    }

    @Override
    public ApiResponse deleteById(Long commentId, UserPrincipal userPrincipal) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(AppConstant.COMMENT_NOT_FOUND + commentId)
                );
        User user = userRepository.findByComments(comment);
        if (user.getId().equals(userPrincipal.getId()) || userPrincipal.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ " + RoleName.ADMIN.toString()))) {
            commentRepository.delete(comment);
            return new ApiResponse(Boolean.TRUE, AppConstant.COMMENT_DELETE_MESSAGE, HttpStatus.OK);

        }
        throw new AccessDeniedException(AppConstant.COMMENT_DELETE_DENY);
    }

    @Override
    public ApiResponse deleteCommentsByBlog(Long blogId, UserPrincipal userPrincipal) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(AppConstant.BLOG_NOT_FOUND + blogId)
                );
        User user = userRepository.findByBlogs(blog);
        if (user.getId().equals(userPrincipal.getId()) || userPrincipal.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ " + RoleName.ADMIN))) {
            commentRepository.deleteAllByBlog(blog);
            return new ApiResponse(Boolean.TRUE, AppConstant.COMMENT_DELETE_MESSAGE, HttpStatus.OK);
        }
        throw new AccessDeniedException(AppConstant.COMMENT_DELETE_DENY);
    }

    @Override
    public CommentResponse updateCommentById(Long commentId, CommentRequest commentRequest, UserPrincipal userPrincipal) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(AppConstant.COMMENT_NOT_FOUND + commentId)
                );
        User user = userRepository.findByComments(comment);
        if (user.getId().equals(userPrincipal.getId()) || userPrincipal.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ " + RoleName.ADMIN))) {
            modelMapper.map(commentRequest, comment);
            Comment savedComment = commentRepository.save(comment);

            CommentResponse commentResponse = modelMapper.map(savedComment, CommentResponse.class);
            return commentResponse;
        }

        throw new AccessDeniedException(AppConstant.COMMENT_UPDATE_DENY);

    }

    @Override
    public PageResponse<CommentResponse> getCommentsByUsername(String username, Integer page, Integer size) {
        return null;
    }

    @Override
    public CommentResponse addComment(Long blogId, CommentRequest commentRequest, UserPrincipal userPrincipal) {
        Comment comment = modelMapper.map(commentRequest, Comment.class);

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(AppConstant.BLOG_NOT_FOUND)
                );
        User user = modelMapper.map(userPrincipal, User.class);
        comment.setUser(user);
        comment.setBlog(blog);
        commentRepository.save(comment);
        CommentResponse commentResponse = modelMapper.map(comment, CommentResponse.class);

        return commentResponse;

    }
}

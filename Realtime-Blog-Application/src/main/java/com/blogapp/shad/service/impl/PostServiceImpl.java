package com.blogapp.shad.service.impl;
import com.blogapp.shad.entities.Category;
import com.blogapp.shad.entities.Post;
import com.blogapp.shad.exception.ResourceNotFoundException;
import com.blogapp.shad.payload.PostDto;
import com.blogapp.shad.payload.PostResponse;
import com.blogapp.shad.repository.CategoryRepository;
import com.blogapp.shad.repository.PostRepository;
import com.blogapp.shad.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private CategoryRepository categoryRepository;
    private ModelMapper mapper;

    public PostServiceImpl(PostRepository postRepository,
                           CategoryRepository categoryRepository,
                           ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.mapper=modelMapper;
    }

    /**
     *
     * @param post
     * @return
     * Convert Entity to DTO
     */

    private PostDto mapToDto(Post post){
  PostDto postDto=mapper.map(post,PostDto.class);
  return postDto;
    }

    /**
     *
     * @param postDto
     * @return
     */

    private Post mapToEntity(PostDto postDto){
        Post post=mapper.map(postDto,Post.class);
        return post;
    }

    /** Create Post based on category Id If category Id is not found it throw exception
     * Category Not Found with This Id:
     *
     * @return
     */


    @Override
    public PostDto createPost(PostDto postDto) {
        Category category=categoryRepository.findById(postDto
                .getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category","id",postDto.getCategoryId()));
        /**
         * Convert DTO to Entity to save Post into the Database
         */
        Post post=mapToEntity(postDto);
        post.setCategory(category);
        Post newPost= postRepository.save(post);
        log.info("Save Post Successfully {} ",post);
        /**
         * Convert Entity to DTO
         */
        PostDto postResponse=mapToDto(newPost);
        return postResponse;
    }

    /**
     *
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort=sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy):Sort.by(sortBy).descending();
        /**
         * Create Pageable Object
         */
        PageRequest pageable=PageRequest.of(pageNo,pageSize,sort);
        Page<Post> posts=postRepository.findAll(pageable);
        /**
         * get Content from page object
         */
        List<Post> listOfPosts=posts.getContent();
        List<PostDto> content=listOfPosts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
        PostResponse postResponse=new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());
        return postResponse;
    }

    /**
     *
     * @param id
     * @return
     */

    @Override
    public PostDto getPostById(Long id) {
        Post post=postRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Post","id",id));
        return mapToDto(post);
    }

    /**
     *
     * @param postDto Post  to be update here based on Post id with Post Body
     * @param id
     * @return
     */

    @Override
    public PostDto updatePost(PostDto postDto, Long id) {
        /**
         * Get Post by Id from Database
         */
       Post post=postRepository.findById(id).orElseThrow(
               ()-> new ResourceNotFoundException("Post","id",id));
      Category category=categoryRepository.findById(postDto.getCategoryId())
              .orElseThrow(()-> new ResourceNotFoundException("Category","id", postDto.getCategoryId()));
       post.setTitle(postDto.getTitle());
       post.setDescription(postDto.getDescription());
       post.setContent(postDto.getContent());
       post.setCategory(category);
       Post updatedPost=postRepository.save(post);
       log.info("Updated Post Based On Id {} ",post);
        return mapToDto(updatedPost);
    }

    /**
     * Delete Post based on Post Id
     * @param id
     */

    @Override
    public void deletePostById(Long id) {
        /**
         * Get Post by id from the database
         */
        Post post=postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post","id",id));

        postRepository.delete(post);
        log.info("Delete Post By Id {} ",post);

    }

    /**
     *
     * @param categoryId
     * @return
     */

    @Override
    public List<PostDto> getPostsByCategory(Long categoryId) {
        /**
         * Fetch Category by id from the database
         */
        Category category=categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Category","id",categoryId));
        List<Post> posts=postRepository.findByCategoryId(categoryId);
        return posts.stream().map(post ->
                mapToDto(post)).collect(Collectors.toList());
    }

    /**
     *
     * @param query
     * @return
     */

    @Override
    public List<PostDto> searchProducts(String query) {
     List<Post> posts=postRepository.searchProducts(query);
        return posts.stream().map(post -> mapToDto(post)).
                collect(Collectors.toList());
    }
}

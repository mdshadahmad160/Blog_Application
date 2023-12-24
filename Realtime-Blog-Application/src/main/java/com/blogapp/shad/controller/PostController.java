package com.blogapp.shad.controller;
import com.blogapp.shad.payload.PostDto;
import com.blogapp.shad.payload.PostResponse;
import com.blogapp.shad.service.PostService;
import com.blogapp.shad.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author Shad Ahmad
 * @since 24/12/2023
 */

@RestController
@RequestMapping()
public class PostController {


    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     *
     * @param postDto Saved into the database
     * @return
     */
    @PostMapping("api/v1/posts")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);

}

    /**
     *
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return Get All Post RestAPI
     */
   @GetMapping("api/v1/posts")
    public PostResponse getAllPosts(@RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
                                    @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
                                    @RequestParam(value = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false) String sortBy,
                                    @RequestParam(value = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false) String sortDir)
    {
      return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }
    /**
     * Get Post By Id
     */
    @GetMapping("api/v1/posts/{id}")
   public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") Long id){
       return new ResponseEntity<>(postService.getPostById(id),HttpStatus.OK);
   }

    /**
     *
     * @param postDto
     * @param id
     * @return Update Post By id with Post Body
     */
    @PutMapping("api/v1/posts/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto,
                                              @PathVariable(name = "id") Long id){
     PostDto postResponse=postService.updatePost(postDto,id);
     return new ResponseEntity<>(postResponse,HttpStatus.OK);
    }

    /**
     *
     * @param id
     * @return Delete Post RestAPI based on Post id
     */
    @DeleteMapping("api/v1/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") Long id){
        postService.deletePostById(id);
        return new ResponseEntity<>("Post Deleted Successfully ",HttpStatus.OK);
    }

    /**
     *
     * @param categoryId
     * @return Get List of post based on category id RESTAPI
     */
    @GetMapping("api/v1/posts/category/{id}")
    public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable(name = "id") Long categoryId){
        List<PostDto>  postDtos=postService.getPostsByCategory(categoryId);
        return new ResponseEntity<>(postDtos,HttpStatus.OK);
    }

    /**
     *
     * @param query
     * @return Search products based on query RESTAPI
     */
    @GetMapping("api/v1/posts/search")
    public ResponseEntity<List<PostDto>> searchPosts(@RequestParam(name = "query") String query){
    return ResponseEntity.ok(postService.searchProducts(query));
    }

}

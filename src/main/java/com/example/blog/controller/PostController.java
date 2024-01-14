package com.example.blog.controller;

import com.example.blog.dto.PostDto;
import com.example.blog.dto.PostResponse;
import com.example.blog.service.PostService;
import com.example.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@Tag(
        name = "CRUD rest api post",  // tiêu đề cho post swagger-ui
        description = "Description crud rest api post hihi"
)
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping
    public ResponseEntity<PostResponse> getAllPosts(@RequestParam(name = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
                                                    @RequestParam(name = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
                                                    @RequestParam(name = "sortBy",defaultValue = AppConstants.DEFAULT_SOFT_BY,required = false) String sortBy,
                                                    @RequestParam(name = "sortDir",defaultValue = AppConstants.DEFAULT_SOFT_DIRECTION,required = false) String sortDir){
        PostResponse postResponse = postService.getAllPosts(pageNo,pageSize,sortBy,sortDir);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable long id){
        return ResponseEntity.ok(postService.getPostDtoById(id));
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<List<PostDto>> getPostByCategory(@PathVariable long id){
        return ResponseEntity.ok(postService.getPostsByCategory(id));
    }

    //SecurityRequirement thêm 1 cái khóa bảo mật ở chỗ ui để xác thực jwt
    @SecurityRequirement(name = "Bear Authentication jwt")
    @Operation(
            summary = "Create Post Rest API",
            description = "Create post rest api used to save post into database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
        PostDto createPost = postService.createPost(postDto);
        return new ResponseEntity<>(createPost, HttpStatus.CREATED);
    }
    @SecurityRequirement(name = "Bear Authentication jwt")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable long id){
        PostDto updatePost = postService.updatePost(postDto,id);
        return new ResponseEntity<>(updatePost, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Bear Authentication jwt")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable long id){
        String message = postService.deletePost(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}

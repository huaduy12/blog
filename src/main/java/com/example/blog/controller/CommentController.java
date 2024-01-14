package com.example.blog.controller;

import com.example.blog.dto.CommentDto;
import com.example.blog.dto.CommentResponse;
import com.example.blog.service.CommentService;
import com.example.blog.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;

    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<CommentResponse> getAllPosts(@PathVariable("postId") long idPost,
            @RequestParam(name = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
                                                    @RequestParam(name = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
                                                    @RequestParam(name = "sortBy",defaultValue = AppConstants.DEFAULT_SOFT_BY,required = false) String sortBy,
                                                    @RequestParam(name = "sortDir",defaultValue = AppConstants.DEFAULT_SOFT_DIRECTION,required = false) String sortDir){
        CommentResponse commentResponse = commentService.getAllComments(idPost,pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(commentResponse);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("id") long id){
        return ResponseEntity.ok(commentService.getCommentDtoById(id));
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable("postId") long postId,@Valid @RequestBody CommentDto commentDto){
        return new ResponseEntity<>(commentService.createComment(postId,commentDto), HttpStatus.CREATED);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("id") long id,@Valid @RequestBody CommentDto commentDto){
        return ResponseEntity.ok(commentService.updateComment(commentDto,id));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") long id){
        return ResponseEntity.ok(commentService.deleteComment(id));
    }
}

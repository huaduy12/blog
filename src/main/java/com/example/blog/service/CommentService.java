package com.example.blog.service;

import com.example.blog.dto.CommentDto;
import com.example.blog.dto.CommentResponse;

public interface CommentService {

      CommentResponse getAllComments(long idPost,int pageNo, int pageSize, String sortBy, String sortDir);
     CommentDto getCommentDtoById(long id);
     CommentDto createComment(long idPost, CommentDto commentDto);
     CommentDto updateComment(CommentDto commentDto,long id);
     String deleteComment(long id);
}

package com.example.blog.service.impl;

import com.example.blog.dto.CommentDto;
import com.example.blog.dto.CommentResponse;
import com.example.blog.entity.Comment;
import com.example.blog.entity.Post;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,PostRepository postRepository,ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;

    }


    @Override
    public CommentDto createComment(long idPost, CommentDto commentDto) {
        Post post = getPostById(idPost);
        Comment comment = mapToEntity(commentDto);
        comment.setPost(post);
        Comment newComment =  commentRepository.save(comment);

        return mapToDto(newComment);
    }

    @Override
    public CommentDto updateComment(CommentDto commentDto, long id) {
        Comment comment = getCommentById(id);
        comment.setName(commentDto.getName());
        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());

        Comment commentUpdate = commentRepository.save(comment);
        return mapToDto(commentUpdate);
    }

    @Override
    public String deleteComment(long id) {
        Comment comment = getCommentById(id);
        commentRepository.delete(comment);
        return "Delete comment success";
    }

    public Post getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",id));
        return post;
    }

    public Comment getCommentById(long id){
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment","id",id));
        return comment;
    }

    @Override
    public CommentResponse getAllComments(long idPost,int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo-1,pageSize,sort);
        Page<Comment> comments = commentRepository.findByPost_Id(idPost,pageable);

        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setContent(mapToListCommentDto(comments.getContent()));
        commentResponse.setPageNo(comments.getNumber()+1);
        commentResponse.setPageSize(comments.getSize());
        commentResponse.setTotalPages(comments.getTotalPages());
        commentResponse.setTotalElements(comments.getTotalElements());
        commentResponse.setLast(comments.isLast());
        return commentResponse;
    }

    public CommentDto getCommentDtoById(long id){
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment","id",id));
        return mapToDto(comment);
    }

    private CommentDto mapToDto(Comment comment){
        return modelMapper.map(comment,CommentDto.class);
    }

    private Comment mapToEntity(CommentDto commentDto){
        return modelMapper.map(commentDto,Comment.class);
    }

    private List<CommentDto> mapToListCommentDto(List<Comment> comments){
        List<CommentDto> commentListDto = modelMapper.map(comments,new TypeToken<List<CommentDto>>(){}.getType());
        return commentListDto;
    }

}

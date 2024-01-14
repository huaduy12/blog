package com.example.blog.service;

import com.example.blog.dto.PostDto;
import com.example.blog.dto.PostResponse;
import com.example.blog.entity.Post;

import java.util.List;

public interface PostService {
    PostResponse getAllPosts(int pageNo, int pageSize,String sortBy,String sortDir);
    List<PostDto> getPostsByCategory(Long categoryId);
    PostDto getPostDtoById(long id);
    Post getPostById(long id);
    PostDto createPost(PostDto postDto);

    PostDto updatePost(PostDto postDto,long id);
    String deletePost(long id);
}

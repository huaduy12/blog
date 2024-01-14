package com.example.blog.service.impl;

import com.example.blog.dto.PostDto;
import com.example.blog.dto.PostResponse;
import com.example.blog.entity.Category;
import com.example.blog.entity.Post;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapper modelMapper;
    private CategoryRepository categoryRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize,String sortBy,String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable page = PageRequest.of(pageNo-1,pageSize, sort);
        Page<Post> posts = postRepository.findAll(page);

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(posts.getContent().stream().map(post -> mapToDto(post)).collect(Collectors.toList()));
        postResponse.setPageNo(posts.getNumber()+1);
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setLast(posts.isLast());
        return postResponse;
    }

    @Override
    public List<PostDto> getPostsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category","id",categoryId));

        List<Post> posts = postRepository.getPostByCategory_Id(category.getId());

        return modelMapper.map(posts,new TypeToken<List<PostDto>>(){}.getType());
    }

    @Override
    public PostDto getPostDtoById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",id));
        return mapToDto(post);
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category","id",postDto.getCategoryId()));
        // convert dto to new
        Post post = mapToEntity(postDto);
        post.setCategory(category);
        Post newPost = postRepository.save(post);

        // convert new to dto
        PostDto postResponse = mapToDto(newPost);
        return postResponse;
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category","id",postDto.getCategoryId()));

        Post post = getPostById(id);
        post.setDescription(postDto.getDescription());
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setCategory(category);
        postRepository.save(post);

        return mapToDto(post);
    }

    @Override
    public String deletePost(long id) {
        Post post = getPostById(id);
        postRepository.delete(post);
        return "Delete success id: " + id;
    }

    @Override
    public Post getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",id));
        return post;
    }

    // convert to Dto
    private PostDto mapToDto(Post post){
        return modelMapper.map(post,PostDto.class);
    }

    // convert to Entity
    private Post mapToEntity(PostDto postDto){
        return modelMapper.map(postDto,Post.class);
    }
    private List<PostDto> mapToListPostDto(List<Post> posts){
        List<PostDto> postDtos =  modelMapper.map(posts,new TypeToken<List<PostDto>>(){}.getType());
        return postDtos;
    }

}

package com.example.blog.service.impl;

import com.example.blog.dto.CategoryDto;
import com.example.blog.dto.CategoryResponse;
import com.example.blog.entity.Category;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto,Category.class);
        Category categoryNew = categoryRepository.save(category);
        return modelMapper.map(categoryNew,CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, long id) {
        Category category = findById(id);
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        Category categoryUpdate = categoryRepository.save(category);
        return modelMapper.map(categoryUpdate,CategoryDto.class);
    }

    @Override
    public CategoryDto findByIdDto(Long idCategory) {
        Category category = categoryRepository.findById(idCategory).orElseThrow((() -> new ResourceNotFoundException("Category","id",idCategory)));
        CategoryDto categoryDto = modelMapper.map(category,CategoryDto.class);
        return categoryDto;
    }

    @Override
    public Category findById(Long idCategory) {
        return categoryRepository.findById(idCategory).orElseThrow(() -> new ResourceNotFoundException("Category","id",idCategory));
    }

    @Override
    public String deleteCategory(long id) {
        categoryRepository.delete(findById(id));
        return "Delete success id: " + id;
    }

    @Override
    public CategoryResponse getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable page = PageRequest.of(pageNo-1,pageSize, sort);
        Page<Category> categories = categoryRepository.findAll(page);
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categories.getContent().stream().map(category -> modelMapper.map(category,CategoryDto.class)).collect(Collectors.toList()));
        categoryResponse.setPageNo(categories.getNumber()+1);
        categoryResponse.setPageSize(categories.getSize());
        categoryResponse.setTotalPages(categories.getTotalPages());
        categoryResponse.setTotalElements(categories.getTotalElements());
        categoryResponse.setLast(categories.isLast());
        return categoryResponse;
    }
}

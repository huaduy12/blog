package com.example.blog.service;

import com.example.blog.dto.CategoryDto;
import com.example.blog.dto.CategoryResponse;
import com.example.blog.entity.Category;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto,long id);

    CategoryDto findByIdDto(Long idCategory);
    Category findById(Long idCategory);
    public String deleteCategory(long id);

    CategoryResponse getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir);
}

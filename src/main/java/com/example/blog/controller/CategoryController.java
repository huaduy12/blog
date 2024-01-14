package com.example.blog.controller;

import com.example.blog.dto.CategoryDto;
import com.example.blog.dto.CategoryResponse;
import com.example.blog.service.CategoryService;
import com.example.blog.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> addCategory(@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto categoryNew = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(categoryNew, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,@PathVariable Long id){
        CategoryDto categoryUpdated = categoryService.updateCategory(categoryDto,id);
        return new ResponseEntity<>(categoryUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        String response = categoryService.deleteCategory(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getById(@PathVariable long id){
        CategoryDto categoryDto = categoryService.findByIdDto(id);
        return ResponseEntity.ok(categoryDto);
    }

    @GetMapping
    public ResponseEntity<CategoryResponse> getAll(@RequestParam(name = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,required = false) int pageNo,
                                                   @RequestParam(name = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
                                                   @RequestParam(name = "sortBy",defaultValue = AppConstants.DEFAULT_SOFT_BY,required = false) String sortBy,
                                                   @RequestParam(name = "sortDir",defaultValue = AppConstants.DEFAULT_SOFT_DIRECTION,required = false) String sortDir){
        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNo,pageSize,sortBy,sortDir);
        return ResponseEntity.ok(categoryResponse);
    }


}

package com.example.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private long id;

    @NotBlank(message = "Name can't blank")
    private String name;
    @NotBlank(message = "Name can't blank")
    @Size(min = 10,message = "Description must be 10 characters")
    private String description;
}

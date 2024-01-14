package com.example.blog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "PostDto Model Information")
public class PostDto {

    private long id;

    @Schema(description = "Blog Post Title")
    @NotEmpty
    @Size(min = 2,message = "Post title should have at least 2 characters")
    private String title;

    @Schema(description = "Blog Post Description")
    @NotEmpty
    @Size(min = 10,message = "Post description should have at least 10 characters")
    private String description;

    @Schema(description = "Blog Post Content")
    @NotEmpty(message = "Post content is not empty")
    private String content;
    private List<CommentDto> comments = new ArrayList<>();

    @Schema(description = "Blog Post Category")
    @NotNull
    private Long categoryId;
}

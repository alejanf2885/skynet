package com.alejanf.skynet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class CreateCategoryDTO {

    @Size(min = 1, max = 50)
    @NotBlank
    private String name;

    @NotBlank
    private String slug;

    @Size(min = 1, max = 100)
    @NotBlank
    private String description;

    @URL
    private String imageUrl;
}

package org.example.productservice.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.UUID;
import org.springframework.web.multipart.MultipartFile;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class CreateProductRequest {
    @NotNull(message = "name is required")
    String name;

    @Nullable
    String description;

    @Positive(message = "price must be a positive number")
    Double price;

    @UUID(message = "category id not valid uuid format")
    String categoryId;

    @NotNull(message = "image file is required")
    MultipartFile imageFile;
}

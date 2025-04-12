package org.example.paymentservice.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse implements Serializable {
    String id;
    String name;
    String slug;
    String description;
    Double price;
    String image;
    CategoryResponse category;
}

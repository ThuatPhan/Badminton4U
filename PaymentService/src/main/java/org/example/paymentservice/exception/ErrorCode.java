package org.example.paymentservice.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    CATEGORY_ALREADY_EXISTED(HttpStatus.CONFLICT.value(), "Category already existed"),
    FILE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST.value(), "File upload failed"),
    PRODUCT_NAME_ALREADY_EXISTED(HttpStatus.CONFLICT.value(), "Product name already existed"),
    PRODUCT_SLUG_ALREADY_EXISTED(HttpStatus.CONFLICT.value(), "Product slug already existed"),
    CATEGORY_NOT_EXISTED(HttpStatus.NOT_FOUND.value(), "Category not exist"),
    PRODUCT_NOT_EXIST(HttpStatus.BAD_REQUEST.value(), "Product not exist");

    int code;
    String message;
}

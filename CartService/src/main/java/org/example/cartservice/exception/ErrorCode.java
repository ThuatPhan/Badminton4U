package org.example.cartservice.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    CART_NOT_EXIST(HttpStatus.BAD_REQUEST.value(), "Cart not exist"),
    PRODUCT_NOT_EXIST(HttpStatus.BAD_REQUEST.value(), "Product not exist"),
    FEIGN_CLIENT_ERROR(HttpStatus.BAD_REQUEST.value(), "Feign client error");

    int code;

    @NonFinal
    String message;

    public void withMessage(String message) {
        this.message = message;
    }
}

package org.example.orderservice.exception;

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
    CART_ITEM_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "Cart item not found"),
    FEIGN_CLIENT_ERROR(HttpStatus.BAD_REQUEST.value(), "Feign client error"),
    ORDER_NOT_EXIST(HttpStatus.BAD_REQUEST.value(), "Order not exist" ),
    ;

    int code;

    @NonFinal
    String message;

    public void withMessage(String message) {
        this.message = message;
    }
}

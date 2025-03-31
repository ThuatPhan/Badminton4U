package org.example.orderservice.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.example.orderservice.dto.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class CustomErrorDecoder implements ErrorDecoder {
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String errorBody = new String(
                    response.body().asInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );
            var error = objectMapper.readValue(errorBody, ApiResponse.class);

            ErrorCode errorCode = ErrorCode.FEIGN_CLIENT_ERROR;
            errorCode.withMessage(error.getMessage());

            return new AppException(errorCode);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse response body");
        }
    }
}

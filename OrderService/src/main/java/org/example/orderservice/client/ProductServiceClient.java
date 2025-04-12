package org.example.orderservice.client;

import org.example.orderservice.config.FeignClientConfig;
import org.example.orderservice.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "PRODUCT-SERVICE", path = "/api/products", configuration = FeignClientConfig.class)
public interface ProductServiceClient {
    @GetMapping("/internal")
    List<ProductResponse> getProducts(@RequestParam List<String> ids);
}

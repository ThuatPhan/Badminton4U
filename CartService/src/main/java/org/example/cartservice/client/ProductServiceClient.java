package org.example.cartservice.client;

import org.example.cartservice.config.FeignClientConfig;
import org.example.cartservice.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "PRODUCT-SERVICE", path = "/api/products", configuration = FeignClientConfig.class)
public interface ProductServiceClient {
    @GetMapping("/internal/{id}")
    ProductResponse getProduct(@PathVariable String id);

    @GetMapping("/internal")
    List<ProductResponse> getProducts(@RequestParam List<String> ids);
}

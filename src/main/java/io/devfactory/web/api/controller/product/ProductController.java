package io.devfactory.web.api.controller.product;

import io.devfactory.global.common.ApiResponse;
import io.devfactory.web.api.controller.product.request.ProductCreateRequest;
import io.devfactory.web.api.service.product.ProductService;
import io.devfactory.web.api.service.product.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProductController {

  private final ProductService productService;

  @PostMapping("/api/v1/products/new")
  public ApiResponse<ProductResponse> createProduct(
      @Valid @RequestBody ProductCreateRequest request) {
    return ApiResponse.ok(productService.createProduct(request));
  }

  @GetMapping("/api/v1/products/selling")
  public ApiResponse<List<ProductResponse>> getSellingProducts() {
    return ApiResponse.ok(productService.getSellingProducts());
  }

}

package io.devfactory.web.api.service.product;

import io.devfactory.web.api.service.product.response.ProductResponse;
import io.devfactory.web.domain.product.ProductRepository;
import io.devfactory.web.domain.product.ProductSellingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

  private final ProductRepository productRepository;

  public List<ProductResponse> getSellingProducts() {
    final var products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());
    return products.stream().map(ProductResponse::of).toList();
  }
  
}

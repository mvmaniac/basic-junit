package io.devfactory.web.api.service.product;

import io.devfactory.web.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductNumberFactory {

  private final ProductRepository productRepository;

  public String createNextProductNumber() {
    final var latestProductNumber = productRepository.findLatestProductNumber();

    if (latestProductNumber == null) {
      return "001";
    }

    final var latestProductNumberInt = Integer.parseInt(latestProductNumber);
    final var nextProductNumberInt = latestProductNumberInt + 1;

    return String.format("%03d", nextProductNumberInt);
  }

}

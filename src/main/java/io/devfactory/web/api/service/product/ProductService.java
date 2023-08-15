package io.devfactory.web.api.service.product;

import io.devfactory.web.api.service.product.request.ProductCreateServiceRequest;
import io.devfactory.web.api.service.product.response.ProductResponse;
import io.devfactory.web.domain.product.ProductRepository;
import io.devfactory.web.domain.product.ProductSellingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductNumberFactory productNumberFactory;

  // 증가하는 번호 부여 때문에 동시성 이슈 발생 가능? -> 유니크 제약조건으로 실패 처리 or UUID
  @Transactional
  public ProductResponse createProduct(ProductCreateServiceRequest request) {
    final var nextProductNumber = productNumberFactory.createNextProductNumber();

    final var product = request.toEntity(nextProductNumber);
    final var savedProduct = productRepository.save(product);

    return ProductResponse.of(savedProduct);
  }

  public List<ProductResponse> getSellingProducts() {
    final var products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());
    return products.stream().map(ProductResponse::of).toList();
  }

}

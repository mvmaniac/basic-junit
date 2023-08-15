package io.devfactory.web.api.service.product;

import io.devfactory.IntegrationTestSupport;
import io.devfactory.web.api.controller.product.request.ProductCreateRequest;
import io.devfactory.web.domain.product.Product;
import io.devfactory.web.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.devfactory.web.domain.product.ProductSellingStatus.SELLING;
import static io.devfactory.web.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RequiredArgsConstructor
class ProductServiceTest extends IntegrationTestSupport {

  private final ProductService productService;
  private final ProductRepository productRepository;

  @AfterEach
  void tearDown() {
    productRepository.deleteAllInBatch();
  }

  @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
  @Test
  void createProduct() {
    // given
    final var product = this.buildProduct();
    productRepository.save(product);

    final var request = ProductCreateRequest.builder()
        .type(HANDMADE)
        .sellingStatus(SELLING)
        .name("카푸치노")
        .price(5000)
        .build();

    // when
    final var productResponse = productService.createProduct(request.toServiceRequest());

    // then
    assertThat(productResponse)
        .extracting("productNumber", "type", "sellingStatus", "name", "price")
        .contains("002", HANDMADE, SELLING, "카푸치노", 5000);

    final var products = productRepository.findAll();
    assertThat(products).hasSize(2)
        .extracting("productNumber", "type", "sellingStatus", "name", "price")
        .containsExactlyInAnyOrder(
            tuple("001", HANDMADE, SELLING, "아메리카노", 4000),
            tuple("002", HANDMADE, SELLING, "카푸치노", 5000)
        );
  }

  @DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품번호는 001이다.")
  @Test
  void createProductWhenProductsIsEmpty() {
    // given
    final var request = ProductCreateRequest.builder()
        .type(HANDMADE)
        .sellingStatus(SELLING)
        .name("카푸치노")
        .price(5000)
        .build();

    // when
    final var productResponse = productService.createProduct(request.toServiceRequest());

    // then
    assertThat(productResponse)
        .extracting("productNumber", "type", "sellingStatus", "name", "price")
        .contains("001", HANDMADE, SELLING, "카푸치노", 5000);

    final var products = productRepository.findAll();
    assertThat(products).hasSize(1)
        .extracting("productNumber", "type", "sellingStatus", "name", "price")
        .contains(
            tuple("001", HANDMADE, SELLING, "카푸치노", 5000)
        );
  }

  private Product buildProduct() {
    return Product.builder()
        .productNumber("001")
        .type(HANDMADE)
        .sellingStatus(SELLING)
        .name("아메리카노")
        .price(4000)
        .build();
  }

}

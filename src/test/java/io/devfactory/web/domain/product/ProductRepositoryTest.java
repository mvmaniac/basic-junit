package io.devfactory.web.domain.product;

import io.devfactory.IntegrationTestSupport;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.devfactory.web.domain.product.ProductSellingStatus.*;
import static io.devfactory.web.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@RequiredArgsConstructor
@Transactional
class ProductRepositoryTest extends IntegrationTestSupport {

  private final ProductRepository productRepository;

  @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
  @Test
  void findAllBySellingStatusIn() {
    // given
    final var product1 = this.buildProduct("001", SELLING, "아메리카노", 4000);
    final var product2 = this.buildProduct("002", HOLD, "카페라떼", 4500);
    final var product3 = this.buildProduct("003", STOP_SELLING, "팥빙수", 7000);
    productRepository.saveAll(List.of(product1, product2, product3));

    // when
    final var foundProducts = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

    // then
    assertThat(foundProducts).hasSize(2)
        .extracting("productNumber", "name", "sellingStatus")
        .containsExactlyInAnyOrder(
            tuple("001", "아메리카노", SELLING),
            tuple("002", "카페라떼", HOLD)
        );
  }

  @DisplayName("상품번호 리스트로 상품들을 조회한다.")
  @Test
  void findAllByProductNumberIn() {
    // given
    final var product1 = this.buildProduct("001", SELLING, "아메리카노", 4000);
    final var product2 = this.buildProduct("002", HOLD, "카페라떼", 4500);
    final var product3 = this.buildProduct("003", STOP_SELLING, "팥빙수", 7000);
    productRepository.saveAll(List.of(product1, product2, product3));

    // when
    final var foundProducts = productRepository.findAllByProductNumberIn(List.of("001", "003"));

    // then
    assertThat(foundProducts).hasSize(2)
        .extracting("productNumber", "name", "sellingStatus")
        .containsExactlyInAnyOrder(
            tuple("001", "아메리카노", SELLING),
            tuple("003", "팥빙수", STOP_SELLING)
        );
  }

  @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어온다.")
  @Test
  void findLatestProductNumber() {
    // given
    final var targetProductNumber = "003";

    final var product1 = this.buildProduct("001", SELLING, "아메리카노", 4000);
    final var product2 = this.buildProduct("002", HOLD, "카페라떼", 4500);
    final var product3 = this.buildProduct(targetProductNumber, STOP_SELLING, "팥빙수", 7000);
    productRepository.saveAll(List.of(product1, product2, product3));

    // when
    final var latestProductNumber = productRepository.findLatestProductNumber();

    // then
    assertThat(latestProductNumber).isEqualTo(targetProductNumber);
  }

  @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어올 때, 상품이 하나도 없는 경우에는 null을 반환한다.")
  @Test
  void findLatestProductNumberWhenProductIsEmpty() {
    // when
    final var latestProductNumber = productRepository.findLatestProductNumber();

    // then
    assertThat(latestProductNumber).isNull();
  }

  private Product buildProduct(String productNumber, ProductSellingStatus sellingStatus,
      String name, int price) {
    return Product.builder()
        .productNumber(productNumber)
        .type(HANDMADE)
        .sellingStatus(sellingStatus)
        .name(name)
        .price(price)
        .build();
  }

}

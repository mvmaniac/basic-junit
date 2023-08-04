package io.devfactory.web.domain.product;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import java.util.List;

import static io.devfactory.web.domain.product.ProductSellingStatus.*;
import static io.devfactory.web.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@RequiredArgsConstructor
@TestConstructor(autowireMode = ALL)
@ActiveProfiles("test")
@DataJpaTest
class ProductRepositoryTest {

  private final ProductRepository productRepository;

  @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
  @Test
  void findAllBySellingStatusIn() {
    // given
    final var product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
    final var product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
    final var product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
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
    final var product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
    final var product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
    final var product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
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

    final var product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
    final var product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
    final var product3 = createProduct(targetProductNumber, HANDMADE, STOP_SELLING, "팥빙수", 7000);
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

  private Product createProduct(String productNumber, ProductType type,
      ProductSellingStatus sellingStatus, String name, int price) {
    return Product.builder()
        .productNumber(productNumber)
        .type(type)
        .sellingStatus(sellingStatus)
        .name(name)
        .price(price)
        .build();
  }

}

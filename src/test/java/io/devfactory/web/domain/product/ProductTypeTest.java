package io.devfactory.web.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTypeTest {

  @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
  @Test
  void containsStockTypeFalseCase() {
    // given
    final var givenType = ProductType.HANDMADE;

    // when
    final var result = ProductType.containsStockType(givenType);

    // then
    assertThat(result).isFalse();
  }

  @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
  @Test
  void containsStockTypeTrueCase() {
    // given
    final var givenType = ProductType.BAKERY;

    // when
    final var result = ProductType.containsStockType(givenType);

    // then
    assertThat(result).isTrue();
  }

}

package io.devfactory.web.domain.order;

import io.devfactory.web.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static io.devfactory.web.domain.product.ProductSellingStatus.SELLING;
import static io.devfactory.web.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

  @DisplayName("주문 생성 시 상품 리스트에서 주문의 총 금액을 계산한다.")
  @Test
  void calculateTotalPrice() {
    // given
    final var products = List.of(this.createProduct("001", 1000), this.createProduct("002", 2000));

    // when
    final var order = Order.create(products, LocalDateTime.now());

    // then
    assertThat(order.getTotalPrice()).isEqualTo(3000);
  }

  @DisplayName("주문 생성 시 주문 상태는 INIT이다.")
  @Test
  void init() {
    // given
    final var products = List.of(this.createProduct("001", 1000), this.createProduct("002", 2000));

    // when
    final var order = Order.create(products, LocalDateTime.now());

    // then
    assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);
  }

  @DisplayName("주문 생성 시 주문 등록 시간을 기록한다.")
  @Test
  void registeredDateTime() {
    // given
    final var registeredDateTime = LocalDateTime.now();
    final var products = List.of(this.createProduct("001", 1000), this.createProduct("002", 2000));

    // when
    final var order = Order.create(products, registeredDateTime);
    
    // then
    assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDateTime);
  }

  private Product createProduct(String productName, int price) {
    return Product.builder()
        .type(HANDMADE)
        .productNumber(productName)
        .price(price)
        .sellingStatus(SELLING)
        .name("메뉴명")
        .build();
  }

}

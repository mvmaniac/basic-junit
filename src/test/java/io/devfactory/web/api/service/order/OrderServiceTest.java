package io.devfactory.web.api.service.order;

import io.devfactory.web.api.controller.order.request.OrderCreateRequest;
import io.devfactory.web.domain.order.OrderRepository;
import io.devfactory.web.domain.orderproduct.OrderProductRepository;
import io.devfactory.web.domain.product.Product;
import io.devfactory.web.domain.product.ProductRepository;
import io.devfactory.web.domain.product.ProductType;
import io.devfactory.web.domain.stock.Stock;
import io.devfactory.web.domain.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static io.devfactory.web.domain.product.ProductSellingStatus.SELLING;
import static io.devfactory.web.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@RequiredArgsConstructor
@TestConstructor(autowireMode = ALL)
@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

  private final OrderService orderService;

  private final OrderRepository orderRepository;
  private final OrderProductRepository orderProductRepository;
  private final StockRepository stockRepository;
  private final ProductRepository productRepository;

  @AfterEach
  void tearDown() {
    orderProductRepository.deleteAllInBatch();
    orderRepository.deleteAllInBatch();
    stockRepository.deleteAllInBatch();
    productRepository.deleteAllInBatch();
  }

  @DisplayName("주분번호 리스트를 받아 주문을 생성한다.")
  @Test
  void createOrder() {
    // given
    final var registeredDateTime = LocalDateTime.now();

    final var product1 = this.createProduct(BOTTLE, "001", 1000);
    final var product2 = this.createProduct(HANDMADE, "002", 3000);
    final var product3 = this.createProduct(BAKERY, "003", 5000);
    productRepository.saveAll(List.of(product1, product2, product3));

    final var stock1 = Stock.create("001", 5);
    final var stock2 = Stock.create("003", 3);
    stockRepository.saveAll(List.of(stock1, stock2));

    final var request = OrderCreateRequest.builder()
        .productNumbers(List.of("001", "003"))
        .build();

    // when
    final var orderResponse = orderService.createOrder(request.toServiceRequest(), registeredDateTime);

    // then
    assertThat(orderResponse.getId()).isNotNull();

    assertThat(orderResponse)
        .extracting("registeredDateTime", "totalPrice")
        .contains(registeredDateTime, 6000);

    assertThat(orderResponse.getProducts()).hasSize(2)
        .extracting("productNumber", "price")
        .containsExactlyInAnyOrder(
            tuple("001", 1000),
            tuple("003", 5000)
        );
  }

  @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
  @Test
  void createOrderWithDuplicateProductNumbers() {
    // given
    final var registeredDateTime = LocalDateTime.now();

    final var product1 = this.createProduct(BOTTLE, "001", 1000);
    final var product2 = this.createProduct(HANDMADE, "002", 3000);
    final var product3 = this.createProduct(BAKERY, "003", 5000);
    productRepository.saveAll(List.of(product1, product2, product3));

    final var stock1 = Stock.create("001", 5);
    final var stock2 = Stock.create("003", 3);
    stockRepository.saveAll(List.of(stock1, stock2));

    final var request = OrderCreateRequest.builder()
        .productNumbers(List.of("001", "003", "001"))
        .build();

    // when
    final var orderResponse = orderService.createOrder(request.toServiceRequest(), registeredDateTime);

    // then
    assertThat(orderResponse.getId()).isNotNull();

    assertThat(orderResponse)
        .extracting("registeredDateTime", "totalPrice")
        .contains(registeredDateTime, 7000);

    assertThat(orderResponse.getProducts()).hasSize(3)
        .extracting("productNumber", "price")
        .containsExactlyInAnyOrder(
            tuple("001", 1000),
            tuple("003", 5000),
            tuple("001", 1000)
        );
  }

  @DisplayName("재고와 관련된 상품이 포함되어 있는 주분번호 리스트를 받아 주문을 생성한다.")
  @Test
  void createOrderWithStock() {
    // given
    final var registeredDateTime = LocalDateTime.now();

    final var product1 = this.createProduct(BOTTLE, "001", 1000);
    final var product2 = this.createProduct(HANDMADE, "002", 3000);
    final var product3 = this.createProduct(BAKERY, "003", 5000);
    productRepository.saveAll(List.of(product1, product2, product3));

    // HANDMADE를 제외한 나머지 상품은 재고가 존재함
    final var stock1 = Stock.create("001", 5);
    final var stock2 = Stock.create("003", 3);
    stockRepository.saveAll(List.of(stock1, stock2));

    final var request = OrderCreateRequest.builder()
        .productNumbers(List.of("001", "003", "001", "002"))
        .build();

    // when
    final var orderResponse = orderService.createOrder(request.toServiceRequest(), registeredDateTime);

    // then
    assertThat(orderResponse.getId()).isNotNull();

    assertThat(orderResponse)
        .extracting("registeredDateTime", "totalPrice")
        .contains(registeredDateTime, 10_000);

    assertThat(orderResponse.getProducts()).hasSize(4)
        .extracting("productNumber", "price")
        .containsExactlyInAnyOrder(
            tuple("001", 1000),
            tuple("003", 5000),
            tuple("001", 1000),
            tuple("002", 3000)
        );

    final var stocks = stockRepository.findAll();

    assertThat(stocks).hasSize(2)
        .extracting("productNumber", "quantity")
        .containsExactlyInAnyOrder(
            tuple("001", 3),
            tuple("003", 2)
        );
  }

  @DisplayName("재고가 부족한 상품으로 주문을 생성하려는 경우 예외가 발생한다.")
  @Test
  void createOrderWithNoStock() {
    // given
    final var registeredDateTime = LocalDateTime.now();

    final var product1 = this.createProduct(BOTTLE, "001", 1000);
    final var product2 = this.createProduct(HANDMADE, "002", 3000);
    final var product3 = this.createProduct(BAKERY, "003", 5000);
    productRepository.saveAll(List.of(product1, product2, product3));

    final var stock1 = Stock.create("001", 1);
    final var stock2 = Stock.create("003", 3);
    stockRepository.saveAll(List.of(stock1, stock2));

    final var request = OrderCreateRequest.builder()
        .productNumbers(List.of("001", "003", "001", "002"))
        .build();

    // when
    assertThatThrownBy(() -> orderService.createOrder(request.toServiceRequest(), registeredDateTime))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("재고가 부족한 상품이 있습니다.");
  }

  private Product createProduct(ProductType type, String productName, int price) {
    return Product.builder()
        .type(type)
        .productNumber(productName)
        .price(price)
        .sellingStatus(SELLING)
        .name("메뉴명")
        .build();
  }

}

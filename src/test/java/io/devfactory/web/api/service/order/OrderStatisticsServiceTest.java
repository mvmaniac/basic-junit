package io.devfactory.web.api.service.order;

import io.devfactory.IntegrationTestSupport;
import io.devfactory.web.domain.history.MailSendHistory;
import io.devfactory.web.domain.history.MailSendHistoryRepository;
import io.devfactory.web.domain.order.Order;
import io.devfactory.web.domain.order.OrderRepository;
import io.devfactory.web.domain.order.OrderStatus;
import io.devfactory.web.domain.orderproduct.OrderProductRepository;
import io.devfactory.web.domain.product.Product;
import io.devfactory.web.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static io.devfactory.web.domain.product.ProductSellingStatus.SELLING;
import static io.devfactory.web.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
class OrderStatisticsServiceTest extends IntegrationTestSupport {

  private final OrderStatisticsService orderStatisticsService;

  private final OrderRepository orderRepository;
  private final OrderProductRepository orderProductRepository;
  private final ProductRepository productRepository;
  private final MailSendHistoryRepository mailSendHistoryRepository;

  @AfterEach
  void tearDown() {
    orderProductRepository.deleteAllInBatch();
    orderRepository.deleteAllInBatch();
    productRepository.deleteAllInBatch();
    mailSendHistoryRepository.deleteAllInBatch();
  }

  @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
  @Test
  void sendOrderStatisticsMail() {
    // given
    final var now = LocalDateTime.of(2023, 3, 5, 0, 0);

    final var product1 = this.buildProduct("001", 1000);
    final var product2 = this.buildProduct("002", 2000);
    final var product3 = this.buildProduct("003", 3000);

    final var products = List.of(product1, product2, product3);
    productRepository.saveAll(products);

    final var order1 = this.buildPaymentCompletedOrder(LocalDateTime.of(2023, 3, 4, 23, 59, 59), products);
    final var order2 = this.buildPaymentCompletedOrder(now, products);
    final var order3 = this.buildPaymentCompletedOrder(LocalDateTime.of(2023, 3, 5, 23, 59, 59), products);
    final var order4 = this.buildPaymentCompletedOrder(LocalDateTime.of(2023, 3, 6, 0, 0), products);

    // stubbing
    when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
        .thenReturn(true);

    // when
    boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2023, 3, 5), "test@test.com");

    // then
    assertThat(result).isTrue();

    List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
    assertThat(histories).hasSize(1)
        .extracting("content")
        .contains("총 매출 합계는 12000원입니다.");
  }

  private Product buildProduct(String productNumber, int price) {
    return Product.builder()
        .type(HANDMADE)
        .productNumber(productNumber)
        .price(price)
        .sellingStatus(SELLING)
        .name("메뉴 이름")
        .build();
  }

  private Order buildPaymentCompletedOrder(LocalDateTime now, List<Product> products) {
    final var order = Order.builder()
        .products(products)
        .orderStatus(OrderStatus.PAYMENT_COMPLETED)
        .registeredDateTime(now)
        .build();
    return orderRepository.save(order);
  }

}

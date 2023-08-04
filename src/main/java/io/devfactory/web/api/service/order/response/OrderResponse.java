package io.devfactory.web.api.service.order.response;

import io.devfactory.web.api.service.product.response.ProductResponse;
import io.devfactory.web.domain.order.Order;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderResponse {

  private final Long id;
  private final int totalPrice;
  private final LocalDateTime registeredDateTime;
  private final List<ProductResponse> products;

  @Builder
  private OrderResponse(Long id, int totalPrice, LocalDateTime registeredDateTime,
      List<ProductResponse> products) {
    this.id = id;
    this.totalPrice = totalPrice;
    this.registeredDateTime = registeredDateTime;
    this.products = products;
  }

  public static OrderResponse of(Order order) {
    final var products = order.getOrderProducts().stream()
        .map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
        .toList();

    return OrderResponse.builder()
        .id(order.getId())
        .totalPrice(order.getTotalPrice())
        .registeredDateTime(order.getRegisteredDateTime())
        .products(products)
        .build();
  }

}

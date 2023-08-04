package io.devfactory.web.domain.order;


import io.devfactory.web.domain.BaseEntity;
import io.devfactory.web.domain.orderproduct.OrderProduct;
import io.devfactory.web.domain.product.Product;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Table(name = "tb_order")
@Entity
public class Order extends BaseEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column
  private OrderStatus orderStatus;

  @Column
  private int totalPrice;

  @Column
  private LocalDateTime registeredDateTime;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderProduct> orderProducts = new ArrayList<>();

  @Builder
  private Order(List<Product> products, OrderStatus orderStatus, LocalDateTime registeredDateTime) {
    this.orderStatus = orderStatus;
    this.totalPrice = calculateTotalPrice(products);
    this.registeredDateTime = registeredDateTime;
    this.orderProducts = products.stream()
        .map(product -> new OrderProduct(this, product))
        .toList();
  }

  public static Order create(List<Product> products, LocalDateTime registeredDateTime) {
    return Order.builder()
        .orderStatus(OrderStatus.INIT)
        .products(products)
        .registeredDateTime(registeredDateTime)
        .build();
  }

  private int calculateTotalPrice(List<Product> products) {
    return products.stream()
        .mapToInt(Product::getPrice)
        .sum();
  }

}

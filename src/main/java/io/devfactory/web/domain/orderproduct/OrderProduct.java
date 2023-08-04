package io.devfactory.web.domain.orderproduct;

import io.devfactory.web.domain.BaseEntity;
import io.devfactory.web.domain.order.Order;
import io.devfactory.web.domain.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Table(name = "tb_order_product")
@Entity
public class OrderProduct extends BaseEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

  public OrderProduct(Order order, Product product) {
    this.order = order;
    this.product = product;
  }

}

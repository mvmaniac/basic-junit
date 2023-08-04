package io.devfactory.web.domain.stock;


import io.devfactory.web.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Table(name = "tb_stock")
@Entity
public class Stock extends BaseEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column
  private String productNumber;

  @Column
  private int quantity;

  @Builder
  private Stock(String productNumber, int quantity) {
    this.productNumber = productNumber;
    this.quantity = quantity;
  }

  public static Stock create(String productNumber, int quantity) {
    return Stock.builder()
        .productNumber(productNumber)
        .quantity(quantity)
        .build();
  }

  public boolean isQuantityLessThan(int quantity) {
    return this.quantity < quantity;
  }

  public void deductQuantity(int quantity) {
    if (this.isQuantityLessThan(quantity)) {
      throw new IllegalArgumentException("차감할 재고 수량이 없습니다.");
    }
    this.quantity -= quantity;
  }

}

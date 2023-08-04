package io.devfactory.web.domain.product;

import io.devfactory.web.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Table(name = "tb_product")
@Entity
public class Product extends BaseEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private String productNumber;

  @Enumerated(EnumType.STRING)
  private ProductType type;

  @Enumerated(EnumType.STRING)
  private ProductSellingStatus sellingStatus;

  private String name;

  private int price;

  @Builder
  private Product(String productNumber, ProductType type, ProductSellingStatus sellingStatus,
      String name, int price) {
    this.productNumber = productNumber;
    this.type = type;
    this.sellingStatus = sellingStatus;
    this.name = name;
    this.price = price;
  }

}

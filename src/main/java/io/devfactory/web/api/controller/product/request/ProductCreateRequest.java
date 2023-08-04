package io.devfactory.web.api.controller.product.request;

import io.devfactory.web.api.service.product.request.ProductCreateServiceRequest;
import io.devfactory.web.domain.product.Product;
import io.devfactory.web.domain.product.ProductSellingStatus;
import io.devfactory.web.domain.product.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductCreateRequest {

  @NotNull(message = "상품 타입은 필수입니다.")
  private ProductType type;

  @NotNull(message = "상품 판매상태는 필수입니다.")
  private ProductSellingStatus sellingStatus;

  @NotBlank(message = "상품 이름은 필수입니다.")
  private String name;

  @Positive(message = "상품 가격은 양수여야 합니다.")
  private int price;

  @Builder
  private ProductCreateRequest(ProductType type, ProductSellingStatus sellingStatus, String name,
      int price) {
    this.type = type;
    this.sellingStatus = sellingStatus;
    this.name = name;
    this.price = price;
  }

  public ProductCreateServiceRequest toServiceRequest() {
    return ProductCreateServiceRequest.builder()
        .type(type)
        .sellingStatus(sellingStatus)
        .name(name)
        .price(price)
        .build();
  }

  public Product toEntity(String nextProductNumber) {
    return Product.builder()
        .productNumber(nextProductNumber)
        .type(type)
        .sellingStatus(sellingStatus)
        .name(name)
        .price(price)
        .build();
  }

}

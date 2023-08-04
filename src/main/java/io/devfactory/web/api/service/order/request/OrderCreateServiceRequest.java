package io.devfactory.web.api.service.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class OrderCreateServiceRequest {

  private List<String> productNumbers;

  @Builder
  private OrderCreateServiceRequest(List<String> productNumbers) {
    this.productNumbers = productNumbers;
  }

}

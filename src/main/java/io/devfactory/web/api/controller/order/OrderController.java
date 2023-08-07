package io.devfactory.web.api.controller.order;

import io.devfactory.global.common.ApiResponse;
import io.devfactory.web.api.controller.order.request.OrderCreateRequest;
import io.devfactory.web.api.service.order.OrderService;
import io.devfactory.web.api.service.order.response.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class OrderController {

  private final OrderService orderService;

  @PostMapping("/api/v1/orders/new")
  public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
    return ApiResponse.ok(orderService.createOrder(request.toServiceRequest(), LocalDateTime.now()));
  }

}

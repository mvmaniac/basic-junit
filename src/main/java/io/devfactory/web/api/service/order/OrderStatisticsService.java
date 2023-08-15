package io.devfactory.web.api.service.order;

import io.devfactory.web.api.service.mail.MailService;
import io.devfactory.web.domain.order.Order;
import io.devfactory.web.domain.order.OrderRepository;
import io.devfactory.web.domain.order.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderStatisticsService {

  private final OrderRepository orderRepository;
  private final MailService mailService;

  public boolean sendOrderStatisticsMail(LocalDate orderDate, String email) {
    final var orders = orderRepository.findOrdersBy(
        orderDate.atStartOfDay(),
        orderDate.plusDays(1).atStartOfDay(),
        OrderStatus.PAYMENT_COMPLETED);

    final var totalAmount = orders.stream()
        .mapToInt(Order::getTotalPrice)
        .sum();

    final var result = mailService.sendMail("no-reply@cafekiosk.com", email,
        String.format("[매출통계] %s", orderDate),
        String.format("총 매출 합계는 %s원입니다.", totalAmount));

    if (!result) {
      throw new IllegalArgumentException("매출 통계 메일 전송에 실패했습니다.");
    }

    return true;
  }

}

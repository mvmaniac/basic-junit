package io.devfactory.web.api.service.order;

import io.devfactory.web.api.service.order.request.OrderCreateServiceRequest;
import io.devfactory.web.api.service.order.response.OrderResponse;
import io.devfactory.web.domain.order.Order;
import io.devfactory.web.domain.order.OrderRepository;
import io.devfactory.web.domain.product.Product;
import io.devfactory.web.domain.product.ProductRepository;
import io.devfactory.web.domain.product.ProductType;
import io.devfactory.web.domain.stock.Stock;
import io.devfactory.web.domain.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final StockRepository stockRepository;

  // TODO:[yhs] 재고 감소 시 동시성 이슈 문제
  @Transactional
  public OrderResponse createOrder(OrderCreateServiceRequest request,
      LocalDateTime registeredDateTime) {
    final var productNumbers = request.getProductNumbers();
    final var products = this.findProductsBy(productNumbers);

    this.deductStockQuantities(products);

    final var order = Order.create(products, registeredDateTime);
    final var savedOrder = orderRepository.save(order);

    return OrderResponse.of(savedOrder);
  }

  private List<Product> findProductsBy(List<String> productNumbers) {
    final var products = productRepository.findAllByProductNumberIn(productNumbers);
    final var productMap = products.stream().collect(toMap(Product::getProductNumber, Function.identity()));
    return productNumbers.stream()
        .map(productMap::get)
        .toList();
  }

  private void deductStockQuantities(List<Product> products) {
    final var stockProductNumbers = this.extractStockProductNumbers(products);

    final var stockMap = this.createStockMapBy(stockProductNumbers);
    final var productCountingMap = this.createCountingMapBy(stockProductNumbers);

    for (Map.Entry<String, Long> productCounting : productCountingMap.entrySet()) {
      final var stock = stockMap.get(productCounting.getKey());
      final var quantity = productCounting.getValue().intValue();

      if (stock.isQuantityLessThan(quantity)) {
        throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
      }

      stock.deductQuantity(quantity);
    }
  }

  private List<String> extractStockProductNumbers(List<Product> products) {
    return products.stream()
        .filter(product -> ProductType.containsStockType(product.getType()))
        .map(Product::getProductNumber)
        .toList();
  }

  private Map<String, Stock> createStockMapBy(List<String> stockProductNumbers) {
    List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
    return stocks.stream().collect(toMap(Stock::getProductNumber, Function.identity()));
  }

  private Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
    return stockProductNumbers.stream().collect(groupingBy(Function.identity(), counting()));
  }

}

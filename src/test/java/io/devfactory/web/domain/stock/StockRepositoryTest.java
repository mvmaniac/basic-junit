package io.devfactory.web.domain.stock;

import io.devfactory.IntegrationTestSupport;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


@RequiredArgsConstructor
@Transactional
class StockRepositoryTest extends IntegrationTestSupport {

  private final StockRepository stockRepository;

  @DisplayName("상품번호 리스트로 재고를 조회한다.")
  @Test
  void findAllByProductNumberIn() {
    // given
    final var stock1 = Stock.create("001", 1);
    final var stock2 = Stock.create("002", 2);
    final var stock3 = Stock.create("003", 3);

    stockRepository.saveAll(List.of(stock1, stock2, stock3));

    // when
    final var stocks = stockRepository.findAllByProductNumberIn(List.of("001", "002"));

    // then
    assertThat(stocks).hasSize(2)
        .extracting("productNumber", "quantity")
        .containsExactlyInAnyOrder(
            tuple("001", 1),
            tuple("002", 2)
        );
  }

}

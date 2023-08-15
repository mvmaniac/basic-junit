package io.devfactory.code;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GuavaCodeTest {

  @DisplayName("주어진 개수만큼 List를 파티셔닝한다.")
  @Test
  void partitionLearningTest1() {
    // given
    final var integers = List.of(1, 2, 3, 4, 5, 6);

    // when
    final var partition = Lists.partition(integers, 3);

    // then
    assertThat(partition).hasSize(2)
        .isEqualTo(List.of(
            List.of(1, 2, 3),
            List.of(4, 5, 6)
        ));
  }

  @DisplayName("주어진 개수만큼 List를 파티셔닝한다.")
  @Test
  void partitionLearningTest2() {
    // given
    final var integers = List.of(1, 2, 3, 4, 5, 6);

    // when
    final var partition = Lists.partition(integers, 4);

    // then
    assertThat(partition).hasSize(2)
        .isEqualTo(List.of(
            List.of(1, 2, 3, 4),
            List.of(5, 6)
        ));
  }

  @DisplayName("멀티맵 기능 확인")
  @Test
  void multiMapLearningTest() {
    // given
    final var multimap = ArrayListMultimap.<String, String>create();
    multimap.put("커피", "아메리카노");
    multimap.put("커피", "카페라떼");
    multimap.put("커피", "카푸치노");
    multimap.put("베이커리", "크루아상");
    multimap.put("베이커리", "식빵");

    // when
    final var strings = multimap.get("커피");

    // then
    assertThat(strings).hasSize(3)
        .isEqualTo(List.of("아메리카노", "카페라떼", "카푸치노"));
  }

  @DisplayName("멀티맵 기능 확인")
  @TestFactory
  Collection<DynamicTest> multiMapLearningTest2() {
    // given
    final var multimap = ArrayListMultimap.<String, String>create();
    multimap.put("커피", "아메리카노");
    multimap.put("커피", "카페라떼");
    multimap.put("커피", "카푸치노");
    multimap.put("베이커리", "크루아상");
    multimap.put("베이커리", "식빵");

    return List.of(
        DynamicTest.dynamicTest("1개 value 삭제", () -> {
          // when
          multimap.remove("커피", "카푸치노");

          // then
          final var results = multimap.get("커피");
          assertThat(results).hasSize(2)
              .isEqualTo(List.of("아메리카노", "카페라떼"));
        }),
        DynamicTest.dynamicTest("1개 key 삭제", () -> {
          // when
          multimap.removeAll("커피");

          // then
          final var results = multimap.get("커피");
          assertThat(results).isEmpty();
        })
    );
  }

}

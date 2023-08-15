package io.devfactory.web.api.controller.product;

import io.devfactory.ControllerTestSupport;
import io.devfactory.web.api.controller.product.request.ProductCreateRequest;
import io.devfactory.web.api.service.product.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static io.devfactory.web.domain.product.ProductSellingStatus.SELLING;
import static io.devfactory.web.domain.product.ProductType.HANDMADE;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest extends ControllerTestSupport {
  
  @DisplayName("신규 상품을 등록한다.")
  @Test
  void createProduct() throws Exception {
    // given
    final var request = ProductCreateRequest.builder()
        .type(HANDMADE)
        .sellingStatus(SELLING)
        .name("아메리카노")
        .price(4000)
        .build();

    // when, then
    mockMvc.perform(post("/api/v1/products/new")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @DisplayName("신규 상품을 등록할 때 상품 타입은 필수값이다.")
  @Test
  void createProductWithoutType() throws Exception {
    // given
    final var request = ProductCreateRequest.builder()
        .sellingStatus(SELLING)
        .name("아메리카노")
        .price(4000)
        .build();

    // when, then
    mockMvc.perform(post("/api/v1/products/new")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @DisplayName("신규 상품을 등록할 때 상품 판매상태는 필수값이다.")
  @Test
  void createProductWithoutSellingStatus() throws Exception {
    // given
    final var request = ProductCreateRequest.builder()
        .type(HANDMADE)
        .name("아메리카노")
        .price(4000)
        .build();

    // when, then
    mockMvc.perform(post("/api/v1/products/new")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 판매상태는 필수입니다."))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @DisplayName("신규 상품을 등록할 때 상품 이름은 필수값이다.")
  @Test
  void createProductWithoutName() throws Exception {
    // given
    final var request = ProductCreateRequest.builder()
        .type(HANDMADE)
        .sellingStatus(SELLING)
        .price(4000)
        .build();

    // when, then
    mockMvc.perform(post("/api/v1/products/new")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @DisplayName("신규 상품을 등록할 때 상품 가격은 양수이다.")
  @Test
  void createProductWithZeroPrice() throws Exception {
    // given
    final var request = ProductCreateRequest.builder()
        .type(HANDMADE)
        .sellingStatus(SELLING)
        .name("아메리카노")
        .price(0)
        .build();

    // when, then
    mockMvc.perform(post("/api/v1/products/new")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("상품 가격은 양수여야 합니다."))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @DisplayName("판매 상품을 조회한다.")
  @Test
  void getSellingProducts() throws Exception {
    // given
    List<ProductResponse> result = List.of();

    when(productService.getSellingProducts()).thenReturn(result);

    // when, then
    mockMvc.perform(get("/api/v1/products/selling"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("200"))
        .andExpect(jsonPath("$.status").value("OK"))
        .andExpect(jsonPath("$.message").value("OK"))
        .andExpect(jsonPath("$.data").isArray());
  }

}

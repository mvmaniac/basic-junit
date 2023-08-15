package io.devfactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.devfactory.web.api.controller.order.OrderController;
import io.devfactory.web.api.controller.product.ProductController;
import io.devfactory.web.api.service.order.OrderService;
import io.devfactory.web.api.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@TestConstructor(autowireMode = ALL)
@ActiveProfiles("test")
@WebMvcTest(controllers = {
    OrderController.class,
    ProductController.class
})
public abstract class ControllerTestSupport {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @MockBean
  protected OrderService orderService;

  @MockBean
  protected ProductService productService;

}

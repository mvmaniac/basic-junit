package io.devfactory;

import io.devfactory.infra.mail.MailSendClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

// 모든 테스트를 한번에 실행하는 경우 테스트 환경이 틀리다면 각각 테스트 마다 서버를 기동하는데 비용이 많이 듬
// 최대한 테스트 환경을 동일하게 만들기 위해 클래스를 만들어서 사용
// Repository 테스트의 경우에는 @DataJpaTest를 써도 되나
// 모든 테스트를 실행하는 경우 환경이 달라져 서버가 기동하게 됨
// 다만 모든 테스트를 한번에 수행하지 않는다면 상관없을 듯..
// 또한 컨트롤러의 경우에는 ControllerTestSupport 처럼 별도로 만들어서 사용해도 됨
// @Autowired를 쓰는게 편할 것 같은데 그럴경우 굳이 @TestConstructor는 사용할 필요 없음
@TestConstructor(autowireMode = ALL)
@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {

  @MockBean
  protected MailSendClient mailSendClient;

}

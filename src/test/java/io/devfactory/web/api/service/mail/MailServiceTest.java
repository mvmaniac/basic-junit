package io.devfactory.web.api.service.mail;

import io.devfactory.infra.mail.MailSendClient;
import io.devfactory.web.domain.history.MailSendHistory;
import io.devfactory.web.domain.history.MailSendHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

  @Mock
  private MailSendClient mailSendClient;

  @Spy
  private MailSendClient mailSendClientSpy;

  @Mock
  private MailSendHistoryRepository mailSendHistoryRepository;

  // new MailService(mailSendClient, mailSendHistoryRepository) 대신에 아래처럼 사용 가능
  // 여기서는 @Mock, @Spy 예시 때문에 테스트 메소드에서 생성함
  @InjectMocks
  private MailService mailService;

  @DisplayName("메일 전송 테스트")
  @Test
  void sendMail() {
    // given
    final var mailService = new MailService(mailSendClient, mailSendHistoryRepository);

    // @Mock으로 사용한 경우 Mock 객체를 활용, stubbing한 메소드로 사용하므로 나머지 a, b, c 메소드는 호출 안됨
//    when(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
//        .thenReturn(true);

    // BDD 스타일로 아래처럼 가능
    given(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
        .willReturn(true);

    // when
    final var result = mailService.sendMail("", "", "", "");

    // then
    assertThat(result).isTrue();
    verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
  }

  @DisplayName("메일 전송 테스트 Spy")
  @Test
  void sendMailWithSpy() {
    // given
    final var mailService = new MailService(mailSendClientSpy, mailSendHistoryRepository);

    // @Spy으로 사용한 경우 실제 객체를 활용 sendMail 메소드는 stubbing한 메소드로 사용하고, 나머지 a, b, c 메소드는 실제 메소드가 호출 됨
    doReturn(true)
        .when(mailSendClientSpy)
        .sendEmail(anyString(), anyString(), anyString(), anyString());

    // when
    final var result = mailService.sendMail("", "", "", "");

    // then
    assertThat(result).isTrue();
    verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));
  }

}

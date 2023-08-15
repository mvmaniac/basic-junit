package io.devfactory.web.api.service.mail;

import io.devfactory.infra.mail.MailSendClient;
import io.devfactory.web.domain.history.MailSendHistory;
import io.devfactory.web.domain.history.MailSendHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MailService {

  private final MailSendClient mailSendClient;
  private final MailSendHistoryRepository mailSendHistoryRepository;
  
  public boolean sendMail(String fromEmail, String toEmail, String subject, String content) {
    final var result = mailSendClient.sendEmail(fromEmail, toEmail, subject, content);

    if (!result) {
      return false;
    }

    mailSendHistoryRepository.save(MailSendHistory.builder()
        .fromEmail(fromEmail)
        .toEmail(toEmail)
        .subject(subject)
        .content(content)
        .build());

    mailSendClient.a();
    mailSendClient.b();
    mailSendClient.c();

    return true;
  }

}

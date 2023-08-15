package io.devfactory.web.domain.history;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Table(name = "tb_mail_send_history")
@Entity
public class MailSendHistory {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private String fromEmail;
  private String toEmail;
  private String subject;
  private String content;

  @Builder
  private MailSendHistory(String fromEmail, String toEmail, String subject, String content) {
    this.fromEmail = fromEmail;
    this.toEmail = toEmail;
    this.subject = subject;
    this.content = content;
  }

}

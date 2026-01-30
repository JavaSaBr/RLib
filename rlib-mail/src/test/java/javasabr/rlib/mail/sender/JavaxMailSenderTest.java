package javasabr.rlib.mail.sender;

import static org.assertj.core.api.Assertions.assertThat;

import javasabr.rlib.mail.BaseMailTest;
import javasabr.rlib.mail.sender.impl.JavaxMailSender;
import org.junit.jupiter.api.Test;

public class JavaxMailSenderTest extends BaseMailTest {

  @Test
  void shouldSendEmailSuccessfully() {
    var smtpServer = FAKE_SMTP_TEST_CONTAINER;
    var smtpPort = smtpServer.getSmtpPort();
    var smtpUser = smtpServer.getSmtpUser();
    var smtpPassword = smtpServer.getSmtpPassword();

    var config = MailSenderConfig
        .builder()
        .from("from@test.com")
        .host("localhost")
        .port(smtpPort)
        .password(smtpPassword)
        .username(smtpUser)
        .useAuth(true)
        .build();

    var sender = new JavaxMailSender(config);
    sender.send("to@test.com", "Test Subject", "Content");

    assertThat(smtpServer.getEmailCountFrom("from@test.com"))
        .isEqualTo(1);

    sender.send("to@test.com", "Test Subject 2", "Content 2");

    assertThat(smtpServer.getEmailCountFrom("from@test.com"))
        .isEqualTo(2);
  }
}

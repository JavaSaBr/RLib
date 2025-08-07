package javasabr.rlib.mail.sender;

import javasabr.rlib.mail.BaseMailTest;
import javasabr.rlib.mail.sender.impl.JavaxMailSender;
import org.junit.jupiter.api.Assertions;
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

    Assertions.assertEquals(1, smtpServer.getEmailCountFrom("from@test.com"));

    sender.send("to@test.com", "Test Subject 2", "Content 2");

    Assertions.assertEquals(2, smtpServer.getEmailCountFrom("from@test.com"));
  }
}

package javasabr.rlib.testcontainers;

import javasabr.rlib.mail.sender.MailSenderConfig;
import javasabr.rlib.mail.sender.impl.JavaxMailSender;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FakeSMTPTestContainerTest {

    private static final FakeSMTPTestContainer FAKE_SMTP_TEST_CONTAINER = new FakeSMTPTestContainer();

    @BeforeAll
    static void startContainer() {
        FAKE_SMTP_TEST_CONTAINER.start();
        FAKE_SMTP_TEST_CONTAINER.waitForReadyState();
    }

    @AfterAll
    static void stopContainer() {
        FAKE_SMTP_TEST_CONTAINER.stop();
    }

    @Test
    void shouldStartTestContainerAndCheckApi() {

        Assertions.assertEquals(0, FAKE_SMTP_TEST_CONTAINER.getEmailCountFrom("from@test.com"));

        var smtpServer = FAKE_SMTP_TEST_CONTAINER;
        var smtpPort = smtpServer.getSmtpPort();
        var smtpUser = smtpServer.getSmtpUser();
        var smtpPassword = smtpServer.getSmtpPassword();

        var config = MailSenderConfig.builder()
            .from("from@test.com")
            .host("localhost")
            .port(smtpPort)
            .password(smtpPassword)
            .username(smtpUser)
            .useAuth(true)
            .build();

        var sender = new JavaxMailSender(config);
        sender.send("to@test.com", "Test Subject", "Content");

        Assertions.assertEquals(1, FAKE_SMTP_TEST_CONTAINER.getEmailCountFrom("from@test.com"));

        FAKE_SMTP_TEST_CONTAINER.deleteEmails();

        Assertions.assertEquals(0, FAKE_SMTP_TEST_CONTAINER.getEmailCountFrom("from@test.com"));
    }
}

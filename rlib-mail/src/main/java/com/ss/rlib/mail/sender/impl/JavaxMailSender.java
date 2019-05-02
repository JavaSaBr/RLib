package com.ss.rlib.mail.sender.impl;

import static java.util.concurrent.CompletableFuture.runAsync;
import com.ss.rlib.logger.api.Logger;
import com.ss.rlib.logger.api.LoggerManager;
import com.ss.rlib.mail.sender.MailSender;
import com.ss.rlib.mail.sender.MailSenderConfig;
import com.ss.rlib.mail.sender.exception.UncheckedMessagingException;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.*;

public class JavaxMailSender implements MailSender {

    private static final Logger LOGGER = LoggerManager.getLogger(JavaxMailSender.class);

    @Getter
    @Builder
    public static class JavaxMailSenderConfig {

        private int executorMinThreads;
        private int executorMaxThreads;
        private int executorKeepAlive;

        private Executor executor;
    }

    private final Executor executor;
    private final Session session;
    private final InternetAddress from;

    public JavaxMailSender(@NotNull MailSenderConfig config) {
        this(config, JavaxMailSenderConfig.builder()
            .executorKeepAlive(60)
            .executorMinThreads(1)
            .executorMaxThreads(2)
            .build()
        );
    }
    public JavaxMailSender(@NotNull MailSenderConfig config, @NotNull JavaxMailSenderConfig javaxConfig) {

        var prop = new Properties();
        prop.put("mail.smtp.auth", String.valueOf(config.isUseAuth()));
        prop.put("mail.smtp.host", config.getHost());
        prop.put("mail.smtp.port", String.valueOf(config.getPort()));

        if (config.isEnableTtls()) {
            prop.put("mail.smtp.socketFactory.port", String.valueOf(config.getPort()));
            prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.ssl.trust", config.getSslHost());
        }

        var username = config.getUsername();
        var password = config.getPassword();

        this.session = Session.getInstance(prop, new Authenticator() {

            @Override
            protected @NotNull PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            this.from = new InternetAddress(config.getFrom());
        } catch (AddressException e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("Initialized javax mail sender with settings:");
        LOGGER.info("User : " + username);
        LOGGER.info("From : " + config.getFrom());
        LOGGER.info("Server : " + config.getHost() + ":" + config.getPort());
        LOGGER.info("Using SSL : " + config.isEnableTtls());

        if (javaxConfig.getExecutor() != null) {
            this.executor = javaxConfig.getExecutor();
        } else {
            this.executor = new ThreadPoolExecutor(
                javaxConfig.getExecutorMinThreads(),
                javaxConfig.getExecutorMaxThreads(),
                javaxConfig.getExecutorKeepAlive(),
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
            );
        }
    }

    @Override
    public void send(@NotNull String email, @NotNull String subject, @NotNull String content) {

        try {

            var message = new MimeMessage(session);
            message.setFrom(from);
            message.setRecipients(RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject, StandardCharsets.UTF_8.name());

            var mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(content, "text/html; charset=UTF-8");

            var multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new UncheckedMessagingException(e);
        }
    }

    @Override
    public @NotNull CompletableFuture<Void> sendAsync(
        @NotNull String email,
        @NotNull String subject,
        @NotNull String content
    ) {
        return runAsync(() -> send(email, subject, content), executor);
    }
}

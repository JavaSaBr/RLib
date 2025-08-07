package javasabr.rlib.mail.sender;

import javasabr.rlib.mail.sender.exception.UncheckedMessagingException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public interface MailSender {

    /**
     * Send a new email with the subject to the email address.
     *
     * @param email   the target email.
     * @param subject the subject.
     * @param content the email's content.
     * @throws UncheckedMessagingException if something was wrong.
     */
    void send(@NotNull String email, @NotNull String subject, @NotNull String content);

    /**
     * Send a new email with the subject to the email address.
     *
     * @param email   the target email.
     * @param subject the subject.
     * @param content the email's content.
     * @return the async result of sending process.
     * @throws CompletionException -&gt; UncheckedMessagingException if something was wrong.
     */
    @NotNull CompletableFuture<Void> sendAsync(@NotNull String email, @NotNull String subject, @NotNull String content);
}

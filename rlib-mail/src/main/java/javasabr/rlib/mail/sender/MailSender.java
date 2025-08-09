package javasabr.rlib.mail.sender;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import javasabr.rlib.mail.sender.exception.UncheckedMessagingException;

public interface MailSender {

  /**
   * Send a new email with the subject to the email address.
   *
   * @param email the target email.
   * @param subject the subject.
   * @param content the email's content.
   * @throws UncheckedMessagingException if something was wrong.
   */
  void send(String email, String subject, String content);

  /**
   * Send a new email with the subject to the email address.
   *
   * @param email the target email.
   * @param subject the subject.
   * @param content the email's content.
   * @return the async result of sending process.
   * @throws CompletionException -&gt; UncheckedMessagingException if something was wrong.
   */
  CompletableFuture<Void> sendAsync(String email, String subject, String content);
}

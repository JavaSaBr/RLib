package javasabr.rlib.mail.sender.exception;

import jakarta.mail.MessagingException;
import org.jetbrains.annotations.NotNull;

public class UncheckedMessagingException extends RuntimeException {

  public UncheckedMessagingException(@NotNull MessagingException cause) {
    super(cause);
  }
}

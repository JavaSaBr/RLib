package javasabr.rlib.mail.sender.exception;

import jakarta.mail.MessagingException;

public class UncheckedMessagingException extends RuntimeException {

  public UncheckedMessagingException(MessagingException cause) {
    super(cause);
  }
}

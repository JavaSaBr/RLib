package com.ss.rlib.mail.sender.exception;

import javax.mail.MessagingException;

public class UncheckedMessagingException extends RuntimeException {

    public UncheckedMessagingException(MessagingException cause) {
        super(cause);
    }
}

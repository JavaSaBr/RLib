package com.ss.rlib.mail.sender.exception;

import org.jetbrains.annotations.NotNull;

import javax.mail.MessagingException;

public class UncheckedMessagingException extends RuntimeException {

    public UncheckedMessagingException(@NotNull MessagingException cause) {
        super(cause);
    }
}

package com.ss.rlib.mail.test;

import com.ss.rlib.testcontainers.FakeSMTPTestContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseMailTest {

    protected static final FakeSMTPTestContainer FAKE_SMTP_TEST_CONTAINER = new FakeSMTPTestContainer();

    @BeforeAll
    static void runContainers() {
        FAKE_SMTP_TEST_CONTAINER.start();
        FAKE_SMTP_TEST_CONTAINER.waitForReadyState();
    }

    @AfterAll
    static void stopContainers() {
        FAKE_SMTP_TEST_CONTAINER.stop();
    }
}

package com.ss.rlib.testcontainers;

import org.jetbrains.annotations.NotNull;
import org.testcontainers.containers.GenericContainer;

import java.net.http.HttpClient;

public class FakeSMTPTestContainer extends GenericContainer<FakeSMTPTestContainer> {

    public static final String IMAGE = "javasabr/fake-smtp-server";
    public static final String TAG = "latest";

    public static final int SMTP_PORT = 5025;
    public static final int HTTP_PORT = 5080;

    private final HttpClient httpClient;

    private String restUser;
    private String restPassword;
    private String smtpUser;
    private String smtpPassword;

    public FakeSMTPTestContainer() {
        super(IMAGE + ":" + TAG);
        this.smtpUser = "test";
        this.smtpPassword = "test";
        this.restUser = "admin";
        this.restPassword = "admin";
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    protected void configure() {
        addExposedPorts(SMTP_PORT, HTTP_PORT);
        addEnv("SMTP_USER", smtpUser);
        addEnv("SMTP_PASSWORD", smtpPassword);
        addEnv("APP_USER", restUser);
        addEnv("APP_PASSWORD", restPassword);
    }

    public @NotNull FakeSMTPTestContainer withSmtpUser(@NotNull String username) {
        this.smtpUser = username;
        return this;
    }

    public @NotNull FakeSMTPTestContainer withSmtpPassword(@NotNull String password) {
        this.smtpPassword = password;
        return this;
    }

    public @NotNull FakeSMTPTestContainer withRestUser(@NotNull String username) {
        this.restUser = username;
        return this;
    }

    public @NotNull FakeSMTPTestContainer withRestPassword(@NotNull String password) {
        this.restPassword = password;
        return this;
    }
}

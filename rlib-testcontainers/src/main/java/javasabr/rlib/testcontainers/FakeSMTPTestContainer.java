package javasabr.rlib.testcontainers;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.testcontainers.containers.GenericContainer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodySubscribers;
import java.nio.charset.StandardCharsets;

public class FakeSMTPTestContainer extends GenericContainer<FakeSMTPTestContainer> {

    public static final String IMAGE = "javasabr/fake-smtp-server";
    public static final String TAG = "latest";

    private static final int SMTP_PORT = 5025;
    private static final int HTTP_PORT = 5080;

    private final HttpClient httpClient;

    @Getter
    private String smtpUser;

    @Getter
    private String smtpPassword;

    public FakeSMTPTestContainer() {
        super(IMAGE + ":" + TAG);
        this.smtpUser = "test";
        this.smtpPassword = "test";
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    protected void configure() {
        addExposedPorts(SMTP_PORT, HTTP_PORT);
        addEnv("SMTP_USER", smtpUser);
        addEnv("SMTP_PASSWORD", smtpPassword);
    }

    public int getSmtpPort() {
        return getMappedPort(SMTP_PORT);
    }

    public @NotNull FakeSMTPTestContainer withSmtpUser(@NotNull String username) {
        this.smtpUser = username;
        return this;
    }

    public @NotNull FakeSMTPTestContainer withSmtpPassword(@NotNull String password) {
        this.smtpPassword = password;
        return this;
    }

    /**
     * Gets count of received emails from the sender.
     *
     * @param email the sender's email.
     * @return the emails count or -1 if the request was failed.
     */
    public long getEmailCountFrom(@NotNull String email) {

        var request = HttpRequest.newBuilder(URI.create(getBaseUrl() + "/count/email/from/" + email))
            .GET()
            .build();

        try {

            var response = httpClient.send(
                request,
                responseInfo -> BodySubscribers.ofString(StandardCharsets.UTF_8)
            );

            if (response.statusCode() != 200) {
                return -1;
            }

            return Long.parseLong(response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete all email from this server.
     *
     * @return true if it was successful.
     */
    public boolean deleteEmails() {

        var request = HttpRequest.newBuilder(URI.create(getBaseUrl() + "/emails"))
            .DELETE()
            .build();

        try {

            var response = httpClient.send(
                request,
                responseInfo -> BodySubscribers.discarding()
            );

            return response.statusCode() == 200;

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private @NotNull String getBaseUrl() {
        return "http://localhost:" + getMappedPort(HTTP_PORT);
    }

    public void waitForReadyState() {
        while (!isRunning()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

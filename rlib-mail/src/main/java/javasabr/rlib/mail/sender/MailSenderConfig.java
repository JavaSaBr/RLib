package javasabr.rlib.mail.sender;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MailSenderConfig {

    private String host;
    private int port;

    private String sslHost;
    private String username;
    private String password;
    private String from;

    private boolean useAuth;
    private boolean enableTtls;
}

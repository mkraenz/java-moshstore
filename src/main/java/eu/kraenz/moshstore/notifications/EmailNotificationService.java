package eu.kraenz.moshstore.notifications;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("email")
public class EmailNotificationService implements NotificationService {
    @Value("${smtp.host}")
    private String smtpHost;
    @Value("${smtp.port}")
    private int smtpPort;

    @Override
    public void send(String message, String recipientEmail) {
        System.out.println("Sending email to %s. message: %s".formatted(recipientEmail, message));
    }
}

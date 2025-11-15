package eu.kraenz.moshstore.notifications;

public interface NotificationService {
    void send(String message, String recipientEmail);
}

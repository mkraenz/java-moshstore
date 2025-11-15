package eu.kraenz.moshstore.notifications;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class NotificationManager {
    private final NotificationService notificationService;

    public NotificationManager(@Qualifier("email") NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void sendNotification(String message){
        this.notificationService.send(message, "");
    }
}

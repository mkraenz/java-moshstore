package eu.kraenz.moshstore.users;

import eu.kraenz.moshstore.notifications.NotificationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    private NotificationService notificationService;

    public UserService(UserRepository userRepository, @Qualifier("email") NotificationService notificationService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public void registerUser(User user){
        userRepository.save(user);
        String welcome = "Thanks for your registration, %s.".formatted(user.name);
        notificationService.send(welcome, user.email);

    }
}

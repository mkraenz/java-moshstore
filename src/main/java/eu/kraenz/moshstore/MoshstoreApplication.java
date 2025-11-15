package eu.kraenz.moshstore;

import eu.kraenz.moshstore.notifications.NotificationManager;
import eu.kraenz.moshstore.users.User;
import eu.kraenz.moshstore.users.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MoshstoreApplication {

	public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(MoshstoreApplication.class, args);
        var users = ctx.getBean(UserService.class);
        users.registerUser(new User(1234, "hello@example.com", "Peter", "*******"));
//        var orderService = ctx.getBean(OrderService.class);
//        orderService.placeOrder();
//        ctx.close();
//        var notifications = ctx.getBean(NotificationManager.class);
//        notifications.sendNotification("I SEND THIGNS");
    }

}

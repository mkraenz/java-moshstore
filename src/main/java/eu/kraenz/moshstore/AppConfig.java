package eu.kraenz.moshstore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {
    @Value("${paymentProvider}")
    private String paymentProvider;

    @Bean
    public PaymentService stripe() {
        return new StripePaymentService();
    }

    @Bean
    public OrderService orderService() {
        if (paymentProvider.equals("stripe")) {

            return new OrderService(stripe());
        } else if (paymentProvider.equals("paypal")) {
            return new OrderService(paypal());
        } else {
            throw new RuntimeException("Configuration mismatch. 'paymentProvider' must be either 'stripe' or 'paypal'.");
        }
    }

    @Bean
    PaymentService paypal() {
        return new PayPalPaymentService();
    }
}

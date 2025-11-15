package eu.kraenz.moshstore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service("stripe")
//@Primary
public class StripePaymentService implements PaymentService {
    @Value("${stripe.apiUrl}")
    private String stripeApiUrl;

    @Value("${stripe.enabled}")
    private boolean enabled;

    @Value("${stripe.timeout:3000}")
    private int timeout;

    @Value("${stripe.supported-currencies}")
    private List<String> supportedCurrencies;

    @Override
    public void processPayment(double amount){
        System.out.println("ENABLED "+ enabled);
        System.out.println("Timeout "+ timeout);
        System.out.println("url "+ stripeApiUrl);
        System.out.println("supported currencies "+ supportedCurrencies);
        System.out.println("STRIPE");
        System.out.println("Amount: " + amount);
    }
}

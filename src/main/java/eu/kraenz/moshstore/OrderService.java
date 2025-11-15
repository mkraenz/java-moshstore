package eu.kraenz.moshstore;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

//@Service
public class OrderService {

    private PaymentService paymentService;

//    public OrderService(@Qualifier("stripe") PaymentService paymentService) {
    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @PostConstruct
    public void onCreated(){
        System.out.println("OrderSvc created");
    }

    @PreDestroy
    public void onDestroy(){
        System.out.println("OrderSvc onDestroy");
    }

    public void placeOrder(){
        paymentService.processPayment(10);
    }
}

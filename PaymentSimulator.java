package service;

import java.util.Random;

public class PaymentSimulator {

    static Random rand = new Random();

    public static boolean processPayment(double amount) {
        System.out.printf("Processing payment of ₹%.2f...\n", amount);

        try {
            Thread.sleep(600);
        } catch (Exception ignored) {}

        boolean success = rand.nextInt(100) < 70;

        System.out.println(success ? "Payment Successful." : "Payment Failed.");
        return success;
    }
}

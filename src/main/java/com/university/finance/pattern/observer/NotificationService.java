package com.university.finance.pattern.observer;

import com.university.finance.model.Transaction;
import java.text.SimpleDateFormat;

public class NotificationService implements TransactionObserver {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void update(Transaction transaction) {
        String notification = String.format(
                "NOTIFICATION: Transaction effectuée le %s\n" +
                        "Type: %s\n" +
                        "Montant: %.2f €\n" +
                        "Description: %s\n",
                dateFormat.format(transaction.getTimestamp()),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getDescription()
        );

        // Dans une vraie application, on enverrait un email/SMS
        System.out.println("=== NOTIFICATION PAR EMAIL ===");
        System.out.println(notification);
        System.out.println("==============================");
    }
}
package com.university.finance.pattern.observer;

import com.university.finance.model.Transaction;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

public class AuditLogger implements TransactionObserver {
    private static final Logger logger = Logger.getLogger(AuditLogger.class.getName());
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void update(Transaction transaction) {
        String logMessage = String.format(
                "[AUDIT] %s - User: %s, Type: %s, Amount: %.2f, Description: %s",
                dateFormat.format(transaction.getTimestamp()),
                transaction.getUserId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getDescription()
        );

        logger.info(logMessage);
        // Dans une vraie application, on écrirait dans un fichier journal
        System.out.println(logMessage);
    }
}
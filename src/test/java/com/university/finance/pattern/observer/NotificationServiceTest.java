package com.university.finance.pattern.observer;

import com.university.finance.model.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.*;

public class NotificationServiceTest {

    @Test
    public void testNotificationServiceUpdate() {
        // Capture la sortie système
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            NotificationService notificationService = new NotificationService();
            Transaction transaction = new Transaction(
                    "user123",
                    TransactionType.TRANSFER,
                    250.0,
                    "Transfer to friend"
            );

            notificationService.update(transaction);

            String output = outputStream.toString();

            // Vérifie que le message de notification contient les bonnes informations
            assertTrue("Output should contain NOTIFICATION", output.contains("NOTIFICATION"));
            assertTrue("Output should contain email simulation", output.contains("EMAIL"));
            assertTrue("Output should contain TRANSFER", output.contains("TRANSFER"));
            assertTrue("Output should contain amount", output.contains("250.00"));
            assertTrue("Output should contain description", output.contains("Transfer to friend"));
        } finally {
            // Restaure la sortie système originale
            System.setOut(originalOut);
        }
    }
}
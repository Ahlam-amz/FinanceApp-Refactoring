package com.university.finance.pattern.observer;

import com.university.finance.model.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.*;

public class AuditLoggerTest {

    @Test
    public void testAuditLoggerUpdate() {
        // Capture la sortie système
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            AuditLogger auditLogger = new AuditLogger();
            Transaction transaction = new Transaction(
                    "user123",
                    TransactionType.DEPOSIT,
                    500.0,
                    "Test transaction"
            );

            auditLogger.update(transaction);

            String output = outputStream.toString();

            // Vérifie que le message de log contient les informations de transaction
            assertTrue("Output should contain AUDIT", output.contains("[AUDIT]"));
            assertTrue("Output should contain user ID", output.contains("user123"));
            assertTrue("Output should contain DEPOSIT", output.contains("DEPOSIT"));
            assertTrue("Output should contain amount", output.contains("500.00"));
            assertTrue("Output should contain description", output.contains("Test transaction"));
        } finally {
            // Restaure la sortie système originale
            System.setOut(originalOut);
        }
    }
}
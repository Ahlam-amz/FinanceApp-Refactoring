package com.university.finance.model;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;

public class TransactionTest {

    @Test
    public void testTransactionCreation() {
        Transaction transaction = new Transaction(
                "user123",
                TransactionType.DEPOSIT,
                500.0,
                "Test deposit"
        );

        assertNotNull("Transaction ID should not be null", transaction.getId());
        assertEquals("User ID should match", "user123", transaction.getUserId());
        assertEquals("Transaction type should be DEPOSIT",
                TransactionType.DEPOSIT, transaction.getType());
        assertEquals("Amount should be 500", 500.0, transaction.getAmount(), 0.001);
        assertEquals("Description should match", "Test deposit", transaction.getDescription());
        assertNotNull("Timestamp should not be null", transaction.getTimestamp());

        // Vérifier que le timestamp est récent (dans les 5 secondes)
        long timeDifference = Math.abs(new Date().getTime() - transaction.getTimestamp().getTime());
        assertTrue("Timestamp should be recent", timeDifference < 5000);
    }

    @Test
    public void testTransactionToString() {
        Transaction transaction = new Transaction(
                "user123",
                TransactionType.WITHDRAWAL,
                200.0,
                "ATM withdrawal"
        );

        String str = transaction.toString();
        assertTrue("toString should contain transaction ID", str.contains(transaction.getId()));
        assertTrue("toString should contain user ID", str.contains("user123"));
        assertTrue("toString should contain amount", str.contains("200.0"));
        assertTrue("toString should contain type", str.contains("WITHDRAWAL"));
    }

    @Test
    public void testDifferentTransactionTypes() {
        Transaction deposit = new Transaction("user1", TransactionType.DEPOSIT, 100.0, "Deposit");
        Transaction withdrawal = new Transaction("user1", TransactionType.WITHDRAWAL, 50.0, "Withdrawal");
        Transaction transfer = new Transaction("user1", TransactionType.TRANSFER, 75.0, "Transfer");

        assertEquals(TransactionType.DEPOSIT, deposit.getType());
        assertEquals(TransactionType.WITHDRAWAL, withdrawal.getType());
        assertEquals(TransactionType.TRANSFER, transfer.getType());
    }
}
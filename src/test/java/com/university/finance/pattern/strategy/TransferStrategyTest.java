package com.university.finance.pattern.strategy;

import com.university.finance.model.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TransferStrategyTest {
    private TransferStrategy transferStrategy;
    private Account sourceAccount;
    private Account destinationAccount;

    @Before
    public void setUp() {
        transferStrategy = new TransferStrategy();
        sourceAccount = new Account("user123", 1000.0, AccountType.CHECKING);
        destinationAccount = new Account("user456", 500.0, AccountType.SAVINGS);
    }

    @Test
    public void testExecuteTransferSuccess() {
        Transaction transaction = transferStrategy.execute(sourceAccount, destinationAccount, 300.0);

        assertNotNull("Transaction should not be null", transaction);
        assertEquals("Transaction type should be TRANSFER",
                TransactionType.TRANSFER, transaction.getType());
        assertEquals("Transaction amount should be 300",
                300.0, transaction.getAmount(), 0.001);
        assertEquals("Source account balance should decrease",
                700.0, sourceAccount.getBalance(), 0.001);
        assertEquals("Destination account balance should increase",
                800.0, destinationAccount.getBalance(), 0.001);
        assertEquals("Strategy name should be correct",
                "TRANSFER_STRATEGY", transferStrategy.getStrategyName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteTransferInsufficientFunds() {
        transferStrategy.execute(sourceAccount, destinationAccount, 1500.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteTransferNegativeAmount() {
        transferStrategy.execute(sourceAccount, destinationAccount, -100.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteTransferZeroAmount() {
        transferStrategy.execute(sourceAccount, destinationAccount, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExecuteWithOneAccount() {
        transferStrategy.execute(sourceAccount, 100.0);
    }

    @Test
    public void testGetStrategyName() {
        assertEquals("Strategy name should be TRANSFER_STRATEGY",
                "TRANSFER_STRATEGY", transferStrategy.getStrategyName());
    }
}
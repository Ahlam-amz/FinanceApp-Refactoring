package com.university.finance.service;

import com.university.finance.model.*;
import com.university.finance.pattern.strategy.DepositStrategy;
import com.university.finance.pattern.strategy.WithdrawStrategy;
import com.university.finance.pattern.strategy.TransferStrategy;
import com.university.finance.pattern.observer.TransactionNotifier;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class TransactionServiceTest {
    private TransactionService transactionService;
    private TransactionNotifier notifier;
    private Account account1;
    private Account account2;

    @Before
    public void setUp() {
        notifier = new TransactionNotifier();
        transactionService = new TransactionService(notifier);
        account1 = new Account("user1", 1000.0, AccountType.CHECKING);
        account2 = new Account("user2", 500.0, AccountType.SAVINGS);
    }

    @Test
    public void testExecuteDepositTransaction() {
        DepositStrategy depositStrategy = new DepositStrategy();
        Transaction transaction = transactionService.executeTransaction(depositStrategy, account1, 200.0);

        assertNotNull("Transaction should not be null", transaction);
        assertEquals("Transaction type should be DEPOSIT",
                TransactionType.DEPOSIT, transaction.getType());
        assertEquals("Account balance should increase",
                1200.0, account1.getBalance(), 0.001);

        assertEquals("Transaction history should have 1 entry",
                1, transactionService.getTransactionHistory().size());
    }

    @Test
    public void testExecuteWithdrawTransaction() {
        WithdrawStrategy withdrawStrategy = new WithdrawStrategy();
        Transaction transaction = transactionService.executeTransaction(withdrawStrategy, account1, 300.0);

        assertNotNull("Transaction should not be null", transaction);
        assertEquals("Transaction type should be WITHDRAWAL",
                TransactionType.WITHDRAWAL, transaction.getType());
        assertEquals("Account balance should decrease",
                700.0, account1.getBalance(), 0.001);
    }

    @Test
    public void testExecuteTransferTransaction() {
        TransferStrategy transferStrategy = new TransferStrategy();
        Transaction transaction = transactionService.executeTransaction(
                transferStrategy, account1, account2, 400.0);

        assertNotNull("Transaction should not be null", transaction);
        assertEquals("Transaction type should be TRANSFER",
                TransactionType.TRANSFER, transaction.getType());
        assertEquals("Source account balance should decrease",
                600.0, account1.getBalance(), 0.001);
        assertEquals("Destination account balance should increase",
                900.0, account2.getBalance(), 0.001);
    }

    @Test
    public void testGetTransactionHistory() {
        DepositStrategy depositStrategy = new DepositStrategy();
        WithdrawStrategy withdrawStrategy = new WithdrawStrategy();

        transactionService.executeTransaction(depositStrategy, account1, 100.0);
        transactionService.executeTransaction(withdrawStrategy, account1, 50.0);

        List<Transaction> history = transactionService.getTransactionHistory();

        assertEquals("Transaction history should have 2 entries", 2, history.size());
        assertEquals("First transaction should be DEPOSIT",
                TransactionType.DEPOSIT, history.get(0).getType());
        assertEquals("Second transaction should be WITHDRAWAL",
                TransactionType.WITHDRAWAL, history.get(1).getType());
    }

    @Test(expected = UnsupportedOperationException.class)  // CHANGÉ ICI
    public void testInvalidTransactionParameters() {
        DepositStrategy depositStrategy = new DepositStrategy();
        // Trop de paramètres pour un dépôt - devrait lancer UnsupportedOperationException
        transactionService.executeTransaction(depositStrategy, account1, account2, 100.0);
    }
}
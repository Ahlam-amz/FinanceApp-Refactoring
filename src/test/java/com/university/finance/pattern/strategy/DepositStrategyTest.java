package com.university.finance.pattern.strategy;

import com.university.finance.model.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DepositStrategyTest {
    private DepositStrategy depositStrategy;
    private Account testAccount;

    @Before
    public void setUp() {
        depositStrategy = new DepositStrategy();
        testAccount = new Account("user123", 1000.0, AccountType.CHECKING);
    }

    @Test
    public void testExecuteDeposit() {
        Transaction transaction = depositStrategy.execute(testAccount, 500.0);

        assertNotNull("Transaction should not be null", transaction);
        assertEquals("Transaction type should be DEPOSIT",
                TransactionType.DEPOSIT, transaction.getType());
        assertEquals("Transaction amount should be 500",
                500.0, transaction.getAmount(), 0.001);
        assertEquals("Account balance should increase",
                1500.0, testAccount.getBalance(), 0.001);
        assertEquals("Strategy name should be correct",
                "DEPOSIT_STRATEGY", depositStrategy.getStrategyName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteDepositWithNegativeAmount() {
        depositStrategy.execute(testAccount, -100.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteDepositWithZeroAmount() {
        depositStrategy.execute(testAccount, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExecuteWithTwoAccounts() {
        Account account2 = new Account("user456", 500.0, AccountType.SAVINGS);
        depositStrategy.execute(testAccount, account2, 100.0);
    }

    @Test
    public void testGetStrategyName() {
        assertEquals("Strategy name should be DEPOSIT_STRATEGY",
                "DEPOSIT_STRATEGY", depositStrategy.getStrategyName());
    }
}
package com.university.finance.pattern.strategy;

import com.university.finance.model.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WithdrawStrategyTest {
    private WithdrawStrategy withdrawStrategy;
    private Account testAccount;

    @Before
    public void setUp() {
        withdrawStrategy = new WithdrawStrategy();
        testAccount = new Account("user123", 1000.0, AccountType.CHECKING);
    }

    @Test
    public void testExecuteWithdrawSuccess() {
        Transaction transaction = withdrawStrategy.execute(testAccount, 300.0);

        assertNotNull("Transaction should not be null", transaction);
        assertEquals("Transaction type should be WITHDRAWAL",
                TransactionType.WITHDRAWAL, transaction.getType());
        assertEquals("Transaction amount should be 300",
                300.0, transaction.getAmount(), 0.001);
        assertEquals("Account balance should decrease",
                700.0, testAccount.getBalance(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteWithdrawInsufficientFunds() {
        withdrawStrategy.execute(testAccount, 1500.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteWithdrawNegativeAmount() {
        withdrawStrategy.execute(testAccount, -100.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteWithdrawZeroAmount() {
        withdrawStrategy.execute(testAccount, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExecuteWithTwoAccounts() {
        Account account2 = new Account("user456", 500.0, AccountType.SAVINGS);
        withdrawStrategy.execute(testAccount, account2, 100.0);
    }

    @Test
    public void testGetStrategyName() {
        assertEquals("Strategy name should be WITHDRAWAL_STRATEGY",
                "WITHDRAWAL_STRATEGY", withdrawStrategy.getStrategyName());
    }
}
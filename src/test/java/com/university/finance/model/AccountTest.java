package com.university.finance.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
    private Account account;

    @Before
    public void setUp() {
        account = new Account("user123", 1000.0, AccountType.CHECKING);
    }

    @Test
    public void testAccountCreation() {
        assertNotNull("Account ID should not be null", account.getAccountId());
        assertEquals("User ID should match", "user123", account.getUserId());
        assertEquals("Initial balance should be 1000", 1000.0, account.getBalance(), 0.001);
        assertEquals("Account type should be CHECKING", AccountType.CHECKING, account.getType());
    }

    @Test
    public void testDepositPositiveAmount() {
        account.deposit(500.0);
        assertEquals("Balance should increase by 500", 1500.0, account.getBalance(), 0.001);
    }

    @Test
    public void testDepositZeroAmount() {
        account.deposit(0.0);
        assertEquals("Balance should not change", 1000.0, account.getBalance(), 0.001);
    }

    @Test
    public void testDepositNegativeAmount() {
        account.deposit(-100.0);
        assertEquals("Balance should not change with negative deposit",
                1000.0, account.getBalance(), 0.001);
    }

    @Test
    public void testWithdrawSufficientFunds() {
        boolean result = account.withdraw(300.0);
        assertTrue("Withdrawal should succeed", result);
        assertEquals("Balance should decrease by 300", 700.0, account.getBalance(), 0.001);
    }

    @Test
    public void testWithdrawInsufficientFunds() {
        boolean result = account.withdraw(1500.0);
        assertFalse("Withdrawal should fail", result);
        assertEquals("Balance should remain unchanged", 1000.0, account.getBalance(), 0.001);
    }

    @Test
    public void testWithdrawNegativeAmount() {
        boolean result = account.withdraw(-100.0);
        assertFalse("Withdrawal of negative amount should fail", result);
        assertEquals("Balance should remain unchanged", 1000.0, account.getBalance(), 0.001);
    }

    @Test
    public void testWithdrawZeroAmount() {
        boolean result = account.withdraw(0.0);
        assertFalse("Withdrawal of zero should fail", result);
        assertEquals("Balance should remain unchanged", 1000.0, account.getBalance(), 0.001);
    }

    @Test
    public void testSetBalance() {
        account.setBalance(2000.0);
        assertEquals("Balance should be updated to 2000", 2000.0, account.getBalance(), 0.001);
    }
}
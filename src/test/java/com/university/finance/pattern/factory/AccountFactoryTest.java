package com.university.finance.pattern.factory;

import com.university.finance.model.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountFactoryTest {
    private AccountFactory accountFactory = new AccountFactory();

    @Test
    public void testCreateCheckingAccount() {
        Account account = accountFactory.createAccount("user123", 1000.0, "CHECKING");

        assertNotNull("Account should not be null", account);
        assertEquals("User ID should match", "user123", account.getUserId());
        assertEquals("Balance should be 1000", 1000.0, account.getBalance(), 0.001);
        assertEquals("Account type should be CHECKING", AccountType.CHECKING, account.getType());
    }

    @Test
    public void testCreateSavingsAccount() {
        Account account = accountFactory.createAccount("user456", 500.0, "SAVINGS");

        assertNotNull("Account should not be null", account);
        assertEquals("Account type should be SAVINGS", AccountType.SAVINGS, account.getType());
    }

    @Test
    public void testCreateAccountDefaultType() {
        Account account = accountFactory.createAccount("user789", 750.0, "UNKNOWN");

        assertNotNull("Account should not be null", account);
        assertEquals("Default type should be CHECKING", AccountType.CHECKING, account.getType());
    }

    @Test
    public void testCreateCheckingAccountDirectMethod() {
        Account account = accountFactory.createCheckingAccount("user111", 2000.0);

        assertNotNull("Account should not be null", account);
        assertEquals("Account type should be CHECKING", AccountType.CHECKING, account.getType());
    }

    @Test
    public void testCreateSavingsAccountDirectMethod() {
        Account account = accountFactory.createSavingsAccount("user222", 3000.0);

        assertNotNull("Account should not be null", account);
        assertEquals("Account type should be SAVINGS", AccountType.SAVINGS, account.getType());
    }
}
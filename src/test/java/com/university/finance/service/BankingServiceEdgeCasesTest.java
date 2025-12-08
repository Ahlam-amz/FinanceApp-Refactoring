package com.university.finance.service;

import com.university.finance.model.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BankingServiceEdgeCasesTest {
    private BankingService bankingService;

    @Before
    public void setUp() {
        bankingService = new BankingService();
    }

    @Test
    public void testDepositWithZeroAmount() {
        User user = bankingService.authenticateUser("user1", "password1");
        assertNotNull(user);

        var accounts = bankingService.getUserAccounts(user.getUserId());
        String accountId = accounts.get(0).getAccountId();

        // Test avec montant 0
        try {
            bankingService.deposit(accountId, 0.0);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Le montant du dépôt doit être positif", e.getMessage());
        }
    }

    @Test
    public void testDepositWithNegativeAmount() {
        User user = bankingService.authenticateUser("user1", "password1");
        assertNotNull(user);

        var accounts = bankingService.getUserAccounts(user.getUserId());
        String accountId = accounts.get(0).getAccountId();

        try {
            bankingService.deposit(accountId, -100.0);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Le montant du dépôt doit être positif", e.getMessage());
        }
    }

    @Test
    public void testWithdrawWithZeroAmount() {
        User user = bankingService.authenticateUser("user1", "password1");
        assertNotNull(user);

        var accounts = bankingService.getUserAccounts(user.getUserId());
        String accountId = accounts.get(0).getAccountId();

        try {
            bankingService.withdraw(accountId, 0.0);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Le montant du retrait doit être positif", e.getMessage());
        }
    }

    @Test
    public void testGetNonExistentAccount() {
        Account account = bankingService.getAccountById("non-existent-id");
        assertNull("Should return null for non-existent account", account);
    }


    @Test
    public void testGetTransactionHistoryForNewUser() {
        User newUser = bankingService.createUser("newuser_test", "pass", UserType.STANDARD);
        var history = bankingService.getUserTransactionHistory(newUser.getUserId());

        assertNotNull("History should not be null", history);
        assertTrue("History should be empty for new user", history.isEmpty());
    }
}
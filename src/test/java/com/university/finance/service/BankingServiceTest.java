package com.university.finance.service;

import com.university.finance.model.*;
import com.university.finance.pattern.observer.AuditLogger;
import com.university.finance.pattern.observer.NotificationService;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class BankingServiceTest {
    private BankingService bankingService;

    @Before
    public void setUp() {
        bankingService = new BankingService();
        // Ajouter des observateurs pour les tests
        bankingService.getNotifier().addObserver(new AuditLogger());
        bankingService.getNotifier().addObserver(new NotificationService());
    }

    @Test
    public void testInitialization() {
        // Vérifie que l'initialisation par défaut crée des utilisateurs
        User user = bankingService.authenticateUser("user1", "password1");
        assertNotNull("Default user1 should exist", user);
        assertEquals("User should be STANDARD type", UserType.STANDARD, user.getType());

        User premiumUser = bankingService.authenticateUser("user2", "password2");
        assertNotNull("Default user2 should exist", premiumUser);
        assertEquals("User should be PREMIUM type", UserType.PREMIUM, premiumUser.getType());
    }

    @Test
    public void testCreateUser() {
        User newUser = bankingService.createUser("newuser", "newpass", UserType.PREMIUM);

        assertNotNull("New user should not be null", newUser);
        assertEquals("Username should match", "newuser", newUser.getUsername());
        assertEquals("User type should be PREMIUM", UserType.PREMIUM, newUser.getType());

        // Vérifie que l'utilisateur peut s'authentifier
        User authenticated = bankingService.authenticateUser("newuser", "newpass");
        assertNotNull("User should be able to authenticate", authenticated);
        assertEquals("User IDs should match", newUser.getUserId(), authenticated.getUserId());
    }

    @Test
    public void testAuthenticateUserSuccess() {
        User user = bankingService.authenticateUser("user1", "password1");
        assertNotNull("Authentication should succeed with correct credentials", user);
    }

    @Test
    public void testAuthenticateUserFailure() {
        User user = bankingService.authenticateUser("user1", "wrongpassword");
        assertNull("Authentication should fail with wrong password", user);

        User nonExistent = bankingService.authenticateUser("nonexistent", "password");
        assertNull("Authentication should fail for non-existent user", nonExistent);
    }

    @Test
    public void testCreateAccount() {
        User user = bankingService.authenticateUser("user1", "password1");
        assertNotNull("User should exist", user);

        Account account = bankingService.createAccount(user.getUserId(), 2000.0, AccountType.SAVINGS);

        assertNotNull("New account should not be null", account);
        assertEquals("Account should belong to user", user.getUserId(), account.getUserId());
        assertEquals("Account type should be SAVINGS", AccountType.SAVINGS, account.getType());
        assertEquals("Account balance should be 2000", 2000.0, account.getBalance(), 0.001);
    }

    @Test
    public void testGetUserAccounts() {
        User user = bankingService.authenticateUser("user1", "password1");
        assertNotNull("User should exist", user);

        List<Account> accounts = bankingService.getUserAccounts(user.getUserId());
        assertNotNull("Account list should not be null", accounts);
        assertFalse("User should have at least one account", accounts.isEmpty());

        // Vérifie que le compte a le bon solde initial
        Account firstAccount = accounts.get(0);
        assertEquals("Initial balance should be 1000", 1000.0, firstAccount.getBalance(), 0.001);
    }

    @Test
    public void testDepositSuccess() {
        // Récupère un compte existant
        User user = bankingService.authenticateUser("user1", "password1");
        List<Account> accounts = bankingService.getUserAccounts(user.getUserId());
        String accountId = accounts.get(0).getAccountId();

        Transaction transaction = bankingService.deposit(accountId, 500.0);

        assertNotNull("Transaction should not be null", transaction);
        assertEquals("Transaction type should be DEPOSIT",
                TransactionType.DEPOSIT, transaction.getType());
        assertEquals("Transaction amount should be 500",
                500.0, transaction.getAmount(), 0.001);

        // Vérifie que le solde a été mis à jour
        Account updatedAccount = bankingService.getAccountById(accountId);
        assertEquals("Account balance should increase by 500",
                1500.0, updatedAccount.getBalance(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDepositInvalidAccount() {
        bankingService.deposit("nonexistent-account", 500.0);
    }

    @Test
    public void testWithdrawSuccess() {
        User user = bankingService.authenticateUser("user1", "password1");
        List<Account> accounts = bankingService.getUserAccounts(user.getUserId());
        String accountId = accounts.get(0).getAccountId();

        Transaction transaction = bankingService.withdraw(accountId, 300.0);

        assertNotNull("Transaction should not be null", transaction);
        assertEquals("Transaction type should be WITHDRAWAL",
                TransactionType.WITHDRAWAL, transaction.getType());

        Account updatedAccount = bankingService.getAccountById(accountId);
        assertEquals("Account balance should decrease by 300",
                700.0, updatedAccount.getBalance(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithdrawInsufficientFunds() {
        User user = bankingService.authenticateUser("user1", "password1");
        List<Account> accounts = bankingService.getUserAccounts(user.getUserId());
        String accountId = accounts.get(0).getAccountId();

        bankingService.withdraw(accountId, 2000.0); // Plus que le solde
    }

    @Test
    public void testTransferSuccess() {
        User user1 = bankingService.authenticateUser("user1", "password1");
        User user2 = bankingService.authenticateUser("user2", "password2");

        List<Account> accounts1 = bankingService.getUserAccounts(user1.getUserId());
        List<Account> accounts2 = bankingService.getUserAccounts(user2.getUserId());

        String fromAccountId = accounts1.get(0).getAccountId();
        String toAccountId = accounts2.get(0).getAccountId();

        Transaction transaction = bankingService.transfer(fromAccountId, toAccountId, 200.0);

        assertNotNull("Transaction should not be null", transaction);
        assertEquals("Transaction type should be TRANSFER",
                TransactionType.TRANSFER, transaction.getType());

        Account fromAccount = bankingService.getAccountById(fromAccountId);
        Account toAccount = bankingService.getAccountById(toAccountId);

        assertEquals("From account balance should decrease",
                800.0, fromAccount.getBalance(), 0.001);
        assertEquals("To account balance should increase",
                700.0, toAccount.getBalance(), 0.001);
    }

    @Test
    public void testGetUserTransactionHistory() {
        User user = bankingService.authenticateUser("user1", "password1");
        List<Account> accounts = bankingService.getUserAccounts(user.getUserId());
        String accountId = accounts.get(0).getAccountId();

        // Effectue quelques transactions
        bankingService.deposit(accountId, 100.0);
        bankingService.withdraw(accountId, 50.0);

        List<Transaction> history = bankingService.getUserTransactionHistory(user.getUserId());

        assertNotNull("Transaction history should not be null", history);
        assertTrue("Should have at least 2 transactions", history.size() >= 2);

        // Vérifie que les transactions sont dans l'ordre (plus récentes en premier si trié)
        Transaction lastTransaction = history.get(history.size() - 1);
        assertEquals("Last transaction should be WITHDRAWAL",
                TransactionType.WITHDRAWAL, lastTransaction.getType());
    }
}
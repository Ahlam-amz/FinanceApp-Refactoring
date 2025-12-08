package com.university.finance;

import com.university.finance.model.*;
import com.university.finance.service.BankingService;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class AdvancedIntegrationTest {
    private BankingService bankingService;

    @Before
    public void setUp() {
        bankingService = new BankingService();
    }

    @Test
    public void testConsecutiveOperations() {
        User user = bankingService.createUser("testuser", "password", UserType.PREMIUM);
        Account account = bankingService.createAccount(user.getUserId(), 1000.0, AccountType.CHECKING);

        // Série d'opérations
        bankingService.deposit(account.getAccountId(), 500.0);
        bankingService.withdraw(account.getAccountId(), 200.0);
        bankingService.deposit(account.getAccountId(), 300.0);
        bankingService.withdraw(account.getAccountId(), 400.0);

        Account updated = bankingService.getAccountById(account.getAccountId());
        assertEquals("Final balance should be 1200", 1200.0, updated.getBalance(), 0.001);

        List<Transaction> history = bankingService.getUserTransactionHistory(user.getUserId());
        assertEquals("Should have 4 transactions", 4, history.size());
    }

    @Test
    public void testMultipleAccountsPerUser() {
        User user = bankingService.createUser("multiuser", "pass", UserType.STANDARD);

        Account checking = bankingService.createAccount(user.getUserId(), 1000.0, AccountType.CHECKING);
        Account savings = bankingService.createAccount(user.getUserId(), 5000.0, AccountType.SAVINGS);

        // Transfert entre comptes du même utilisateur
        bankingService.transfer(checking.getAccountId(), savings.getAccountId(), 300.0);

        Account updatedChecking = bankingService.getAccountById(checking.getAccountId());
        Account updatedSavings = bankingService.getAccountById(savings.getAccountId());

        assertEquals("Checking balance should be 700", 700.0, updatedChecking.getBalance(), 0.001);
        assertEquals("Savings balance should be 5300", 5300.0, updatedSavings.getBalance(), 0.001);
    }
}
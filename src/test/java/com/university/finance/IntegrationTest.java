package com.university.finance;

import com.university.finance.model.*;
import com.university.finance.pattern.factory.UserFactory;
import com.university.finance.pattern.factory.AccountFactory;
import com.university.finance.service.BankingService;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class IntegrationTest {
    private BankingService bankingService;
    private UserFactory userFactory;
    private AccountFactory accountFactory;

    @Before
    public void setUp() {
        bankingService = new BankingService();
        userFactory = new UserFactory();
        accountFactory = new AccountFactory();
    }

    @Test
    public void testCompleteUserJourney() {
        // 1. Création d'un nouvel utilisateur
        User newUser = userFactory.createPremiumUser("integration_user", "integration_pass");
        User savedUser = bankingService.createUser(
                newUser.getUsername(),
                newUser.getPassword(),
                newUser.getType()
        );

        assertNotNull("User should be created", savedUser);

        // 2. Authentification
        User authenticated = bankingService.authenticateUser("integration_user", "integration_pass");
        assertNotNull("User should authenticate successfully", authenticated);

        // 3. Création d'un compte
        Account account = accountFactory.createSavingsAccount(
                authenticated.getUserId(),
                1000.0
        );
        Account savedAccount = bankingService.createAccount(
                account.getUserId(),
                account.getBalance(),
                account.getType()
        );

        assertNotNull("Account should be created", savedAccount);

        // 4. Opérations bancaires
        bankingService.deposit(savedAccount.getAccountId(), 500.0);
        bankingService.withdraw(savedAccount.getAccountId(), 200.0);

        // 5. Vérification du solde final
        Account updatedAccount = bankingService.getAccountById(savedAccount.getAccountId());
        assertEquals("Final balance should be 1300",
                1300.0, updatedAccount.getBalance(), 0.001);

        // 6. Vérification de l'historique
        List<Transaction> history = bankingService.getUserTransactionHistory(authenticated.getUserId());
        assertTrue("Should have at least 2 transactions", history.size() >= 2);
    }

    @Test
    public void testMultipleUsersAndTransfers() {
        // Création de deux utilisateurs
        User user1 = bankingService.createUser("user_a", "pass_a", UserType.STANDARD);
        User user2 = bankingService.createUser("user_b", "pass_b", UserType.PREMIUM);

        // Création de comptes pour chaque utilisateur
        Account account1 = bankingService.createAccount(user1.getUserId(), 2000.0, AccountType.CHECKING);
        Account account2 = bankingService.createAccount(user2.getUserId(), 1000.0, AccountType.SAVINGS);

        // Transfert entre les comptes
        bankingService.transfer(account1.getAccountId(), account2.getAccountId(), 500.0);

        // Vérification des soldes
        Account updatedAccount1 = bankingService.getAccountById(account1.getAccountId());
        Account updatedAccount2 = bankingService.getAccountById(account2.getAccountId());

        assertEquals("Account1 balance should be 1500", 1500.0, updatedAccount1.getBalance(), 0.001);
        assertEquals("Account2 balance should be 1500", 1500.0, updatedAccount2.getBalance(), 0.001);

        // Vérification des historiques
        List<Transaction> history1 = bankingService.getUserTransactionHistory(user1.getUserId());
        List<Transaction> history2 = bankingService.getUserTransactionHistory(user2.getUserId());

        // MAINTENANT : user2 DOIT avoir un historique car nous créons une transaction pour le destinataire
        assertFalse("User1 should have transaction history (transfer sent)", history1.isEmpty());
        assertFalse("User2 should have transaction history (transfer received)", history2.isEmpty());

        // Vérification des types de transactions
        Transaction user1Transaction = history1.get(0);
        Transaction user2Transaction = history2.get(0);

        assertEquals("User1 transaction should be TRANSFER",
                TransactionType.TRANSFER, user1Transaction.getType());
        assertEquals("User2 transaction should be DEPOSIT (transfer received)",
                TransactionType.DEPOSIT, user2Transaction.getType());
    }
}
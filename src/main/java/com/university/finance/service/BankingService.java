package com.university.finance.service;

import com.university.finance.model.*;
import com.university.finance.pattern.strategy.*;
import com.university.finance.pattern.observer.TransactionNotifier;
import java.util.*;

public class BankingService {
    private Map<String, User> users = new HashMap<>();
    private Map<String, Account> accounts = new HashMap<>();
    private Map<String, List<Transaction>> userTransactions = new HashMap<>();
    private TransactionNotifier notifier = new TransactionNotifier();

    // Stratégies
    private TransactionStrategy depositStrategy = new DepositStrategy();
    private TransactionStrategy withdrawStrategy = new WithdrawStrategy();
    private TransactionStrategy transferStrategy = new TransferStrategy();

    public BankingService() {
        // Initialisation par défaut
        initializeDefaultData();
    }

    private void initializeDefaultData() {
        // Ajouter quelques utilisateurs et comptes par défaut
        User user1 = new User("user1", "password1", UserType.STANDARD);
        User user2 = new User("user2", "password2", UserType.PREMIUM);

        users.put(user1.getUserId(), user1);
        users.put(user2.getUserId(), user2);

        Account account1 = new Account(user1.getUserId(), 1000.0, AccountType.CHECKING);
        Account account2 = new Account(user2.getUserId(), 500.0, AccountType.SAVINGS);

        accounts.put(account1.getAccountId(), account1);
        accounts.put(account2.getAccountId(), account2);

        userTransactions.put(user1.getUserId(), new ArrayList<>());
        userTransactions.put(user2.getUserId(), new ArrayList<>());
    }

    // Gestion des utilisateurs
    public User createUser(String username, String password, UserType type) {
        User user = new User(username, password, type);
        users.put(user.getUserId(), user);
        userTransactions.put(user.getUserId(), new ArrayList<>());
        return user;
    }

    public User authenticateUser(String username, String password) {
        for (User user : users.values()) {
            if (user.getUsername().equals(username) && user.authenticate(password)) {
                return user;
            }
        }
        return null;
    }

    // Gestion des comptes
    public Account createAccount(String userId, double initialBalance, AccountType type) {
        Account account = new Account(userId, initialBalance, type);
        accounts.put(account.getAccountId(), account);
        return account;
    }

    public Account getAccountById(String accountId) {
        return accounts.get(accountId);
    }

    public List<Account> getUserAccounts(String userId) {
        List<Account> userAccounts = new ArrayList<>();
        for (Account account : accounts.values()) {
            if (account.getUserId().equals(userId)) {
                userAccounts.add(account);
            }
        }
        return userAccounts;
    }

    // Opérations bancaires
    public Transaction deposit(String accountId, double amount) {
        Account account = accounts.get(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Compte non trouvé");
        }

        Transaction transaction = depositStrategy.execute(account, amount);
        saveTransaction(transaction);
        notifier.notifyObservers(transaction);

        return transaction;
    }

    public Transaction withdraw(String accountId, double amount) {
        Account account = accounts.get(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Compte non trouvé");
        }

        Transaction transaction = withdrawStrategy.execute(account, amount);
        saveTransaction(transaction);
        notifier.notifyObservers(transaction);

        return transaction;
    }

    public Transaction transfer(String fromAccountId, String toAccountId, double amount) {


        Account fromAccount = accounts.get(fromAccountId);
        Account toAccount = accounts.get(toAccountId);

        if (fromAccount == null || toAccount == null) {
            throw new IllegalArgumentException("Un ou plusieurs comptes non trouvés");
        }

        Transaction transaction = transferStrategy.execute(fromAccount, toAccount, amount);
        saveTransaction(transaction);

        // Créer aussi une transaction pour le destinataire
        Transaction receiverTransaction = new Transaction(
                toAccount.getUserId(),
                TransactionType.DEPOSIT,  // Pour le destinataire, c'est un dépôt
                amount,
                "Transfert reçu de " + fromAccount.getUserId()
        );
        saveTransaction(receiverTransaction);

        notifier.notifyObservers(transaction);
        notifier.notifyObservers(receiverTransaction);

        return transaction;
    }

    private void saveTransaction(Transaction transaction) {
        String userId = transaction.getUserId();
        if (!userTransactions.containsKey(userId)) {
            userTransactions.put(userId, new ArrayList<>());
        }
        userTransactions.get(userId).add(transaction);
    }

    // Getters
    public List<Transaction> getUserTransactionHistory(String userId) {
        return userTransactions.getOrDefault(userId, new ArrayList<>());
    }

    public TransactionNotifier getNotifier() {
        return notifier;
    }
}
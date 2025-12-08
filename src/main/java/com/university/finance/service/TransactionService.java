package com.university.finance.service;

import com.university.finance.model.Transaction;
import com.university.finance.pattern.strategy.TransactionStrategy;
import com.university.finance.pattern.observer.TransactionNotifier;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {
    private TransactionNotifier notifier;
    private List<Transaction> transactionHistory = new ArrayList<>();

    public TransactionService(TransactionNotifier notifier) {
        this.notifier = notifier;
    }

    public Transaction executeTransaction(TransactionStrategy strategy, Object... accounts) {
        Transaction transaction = null;

        if (accounts.length == 2) {
            // Opération sur un seul compte (dépôt/retrait)
            transaction = strategy.execute((com.university.finance.model.Account) accounts[0],
                    (Double) accounts[1]);
        } else if (accounts.length == 3) {
            // Transfert entre deux comptes
            transaction = strategy.execute((com.university.finance.model.Account) accounts[0],
                    (com.university.finance.model.Account) accounts[1],
                    (Double) accounts[2]);
        }

        if (transaction != null) {
            transactionHistory.add(transaction);
            notifier.notifyObservers(transaction);
        }

        return transaction;
    }

    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }
}
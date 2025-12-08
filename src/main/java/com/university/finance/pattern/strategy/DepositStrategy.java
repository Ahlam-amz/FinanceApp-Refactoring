package com.university.finance.pattern.strategy;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import com.university.finance.model.TransactionType;

public class DepositStrategy implements TransactionStrategy {

    @Override
    public Transaction execute(Account account, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Le montant du dépôt doit être positif");
        }

        account.deposit(amount);
        return new Transaction(
                account.getUserId(),
                TransactionType.DEPOSIT,
                amount,
                "Dépôt sur le compte"
        );
    }

    @Override
    public Transaction execute(Account fromAccount, Account toAccount, double amount) {
        throw new UnsupportedOperationException("Le dépôt ne nécessite qu'un seul compte");
    }

    @Override
    public String getStrategyName() {
        return "DEPOSIT_STRATEGY";
    }
}
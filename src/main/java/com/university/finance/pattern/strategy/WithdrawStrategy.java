package com.university.finance.pattern.strategy;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import com.university.finance.model.TransactionType;

public class WithdrawStrategy implements TransactionStrategy {

    @Override
    public Transaction execute(Account account, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Le montant du retrait doit être positif");
        }

        boolean success = account.withdraw(amount);
        if (!success) {
            throw new IllegalArgumentException("Fonds insuffisants pour le retrait");
        }

        return new Transaction(
                account.getUserId(),
                TransactionType.WITHDRAWAL,
                amount,
                "Retrait du compte"
        );
    }

    @Override
    public Transaction execute(Account fromAccount, Account toAccount, double amount) {
        throw new UnsupportedOperationException("Le retrait ne nécessite qu'un seul compte");
    }

    @Override
    public String getStrategyName() {
        return "WITHDRAWAL_STRATEGY";
    }
}
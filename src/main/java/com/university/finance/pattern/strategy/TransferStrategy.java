package com.university.finance.pattern.strategy;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;
import com.university.finance.model.TransactionType;

public class TransferStrategy implements TransactionStrategy {

    @Override
    public Transaction execute(Account account, double amount) {
        throw new UnsupportedOperationException("Le transfert nécessite deux comptes");
    }

    @Override
    public Transaction execute(Account fromAccount, Account toAccount, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Le montant du transfert doit être positif");
        }

        // Retirer du compte source
        boolean success = fromAccount.withdraw(amount);
        if (!success) {
            throw new IllegalArgumentException("Fonds insuffisants pour le transfert");
        }

        // Déposer sur le compte destination
        toAccount.deposit(amount);

        return new Transaction(
                fromAccount.getUserId(),
                TransactionType.TRANSFER,
                amount,
                "Transfert vers " + toAccount.getUserId()
        );
    }

    @Override
    public String getStrategyName() {
        return "TRANSFER_STRATEGY";
    }
}
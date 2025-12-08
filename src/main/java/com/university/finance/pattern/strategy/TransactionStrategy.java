package com.university.finance.pattern.strategy;

import com.university.finance.model.Account;
import com.university.finance.model.Transaction;

public interface TransactionStrategy {
    Transaction execute(Account account, double amount);
    Transaction execute(Account fromAccount, Account toAccount, double amount);
    String getStrategyName();
}
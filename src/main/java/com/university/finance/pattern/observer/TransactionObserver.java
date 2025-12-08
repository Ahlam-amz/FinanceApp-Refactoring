package com.university.finance.pattern.observer;

import com.university.finance.model.Transaction;

public interface TransactionObserver {
    void update(Transaction transaction);
}
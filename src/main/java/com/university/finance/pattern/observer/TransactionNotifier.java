package com.university.finance.pattern.observer;

import com.university.finance.model.Transaction;
import java.util.ArrayList;
import java.util.List;

public class TransactionNotifier {
    private List<TransactionObserver> observers = new ArrayList<>();

    public void addObserver(TransactionObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TransactionObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Transaction transaction) {
        for (TransactionObserver observer : observers) {
            observer.update(transaction);
        }
    }
}
package com.university.finance.pattern.observer;

import com.university.finance.model.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TransactionNotifierTest {
    private TransactionNotifier notifier;
    private MockObserver observer1;
    private MockObserver observer2;

    @Before
    public void setUp() {
        notifier = new TransactionNotifier();
        observer1 = new MockObserver();
        observer2 = new MockObserver();
    }

    @Test
    public void testAddObserver() {
        notifier.addObserver(observer1);
        notifier.addObserver(observer2);

        // Pas de méthode directe pour compter les observateurs, testons via notify
        Transaction transaction = new Transaction("test", TransactionType.DEPOSIT, 100.0, "Test");
        notifier.notifyObservers(transaction);

        assertEquals("Observer1 should be notified once", 1, observer1.getNotificationCount());
        assertEquals("Observer2 should be notified once", 1, observer2.getNotificationCount());
    }

    @Test
    public void testRemoveObserver() {
        notifier.addObserver(observer1);
        notifier.addObserver(observer2);
        notifier.removeObserver(observer1);

        Transaction transaction = new Transaction("test", TransactionType.DEPOSIT, 100.0, "Test");
        notifier.notifyObservers(transaction);

        assertEquals("Observer1 should not be notified after removal", 0, observer1.getNotificationCount());
        assertEquals("Observer2 should be notified", 1, observer2.getNotificationCount());
    }

    @Test
    public void testNotifyObservers() {
        notifier.addObserver(observer1);

        Transaction transaction = new Transaction("user123", TransactionType.WITHDRAWAL, 50.0, "ATM");
        notifier.notifyObservers(transaction);

        assertEquals("Observer should be notified once", 1, observer1.getNotificationCount());
        assertEquals("Observer should receive correct transaction",
                transaction, observer1.getLastTransaction());
    }

    @Test
    public void testNotifyMultipleObservers() {
        notifier.addObserver(observer1);
        notifier.addObserver(observer2);

        Transaction transaction = new Transaction("user456", TransactionType.TRANSFER, 75.0, "Transfer");
        notifier.notifyObservers(transaction);

        assertEquals("Both observers should be notified", 1, observer1.getNotificationCount());
        assertEquals("Both observers should be notified", 1, observer2.getNotificationCount());
        assertEquals("Both observers should receive same transaction",
                observer1.getLastTransaction(), observer2.getLastTransaction());
    }

    // Classe interne pour le test
    private static class MockObserver implements TransactionObserver {
        private int notificationCount = 0;
        private Transaction lastTransaction;

        @Override
        public void update(Transaction transaction) {
            notificationCount++;
            lastTransaction = transaction;
        }

        public int getNotificationCount() {
            return notificationCount;
        }

        public Transaction getLastTransaction() {
            return lastTransaction;
        }
    }
}
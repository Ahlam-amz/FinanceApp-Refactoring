package com.university.finance.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class EdgeCasesTest {

    @Test
    public void testAccountWithNegativeInitialBalance() {
        Account account = new Account("user123", -500.0, AccountType.CHECKING);
        // Le constructeur permet un solde négatif (c'est un problème de conception)
        // Ce test montre qu'on pourrait améliorer la validation
        assertEquals(-500.0, account.getBalance(), 0.001);
    }

    @Test
    public void testTransactionWithZeroAmount() {
        // Le constructeur permet un montant 0 (à améliorer)
        Transaction transaction = new Transaction("user123", TransactionType.DEPOSIT, 0.0, "Zero");
        assertEquals(0.0, transaction.getAmount(), 0.001);
    }
}
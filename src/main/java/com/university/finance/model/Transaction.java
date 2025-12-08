package com.university.finance.model;

import java.util.Date;

public class Transaction {
    private String id;
    private String userId;
    private TransactionType type;
    private double amount;
    private Date timestamp;
    private String description;

    public Transaction(String userId, TransactionType type, double amount, String description) {
        this.id = java.util.UUID.randomUUID().toString();
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.timestamp = new Date();
        this.description = description;
    }

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public Date getTimestamp() { return timestamp; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        // Version simplifiée pour éviter les problèmes avec JaCoCo
        return String.format("Transaction{id='%s', userId='%s', type=%s, amount=%.2f, timestamp=%s}",
                id, userId, type, amount, timestamp.toString());
    }
}
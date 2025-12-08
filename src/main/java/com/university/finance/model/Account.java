package com.university.finance.model;

public class Account {
    private String accountId;
    private String userId;
    private double balance;
    private AccountType type;

    public Account(String userId, double initialBalance, AccountType type) {
        this.accountId = java.util.UUID.randomUUID().toString();
        this.userId = userId;
        this.balance = initialBalance;
        this.type = type;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    // Getters et Setters
    public String getAccountId() { return accountId; }
    public String getUserId() { return userId; }
    public double getBalance() { return balance; }
    public AccountType getType() { return type; }

    public void setBalance(double balance) { this.balance = balance; }
}
package com.university.finance.pattern.factory;

import com.university.finance.model.Account;
import com.university.finance.model.AccountType;

public class AccountFactory {

    public Account createAccount(String userId, double initialBalance, String type) {
        AccountType accountType;

        switch (type.toUpperCase()) {
            case "SAVINGS":
                accountType = AccountType.SAVINGS;
                break;
            case "CHECKING":
            default:
                accountType = AccountType.CHECKING;
                break;
        }

        return new Account(userId, initialBalance, accountType);
    }

    public Account createCheckingAccount(String userId, double initialBalance) {
        return new Account(userId, initialBalance, AccountType.CHECKING);
    }

    public Account createSavingsAccount(String userId, double initialBalance) {
        return new Account(userId, initialBalance, AccountType.SAVINGS);
    }
}
package com.university.finance.pattern.factory;

import com.university.finance.model.User;
import com.university.finance.model.UserType;

public class UserFactory {

    public User createUser(String username, String password, String type) {
        UserType userType;

        switch (type.toUpperCase()) {
            case "PREMIUM":
                userType = UserType.PREMIUM;
                break;
            case "STANDARD":
            default:
                userType = UserType.STANDARD;
                break;
        }

        return new User(username, password, userType);
    }

    public User createStandardUser(String username, String password) {
        return new User(username, password, UserType.STANDARD);
    }

    public User createPremiumUser(String username, String password) {
        return new User(username, password, UserType.PREMIUM);
    }
}
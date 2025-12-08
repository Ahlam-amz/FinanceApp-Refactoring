package com.university.finance.model;

public class User {
    private String userId;
    private String username;
    private String password;
    private UserType type;

    public User(String username, String password, UserType type) {
        this.userId = java.util.UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.type = type;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public UserType getType() { return type; }

    // Pour l'authentification simple
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}
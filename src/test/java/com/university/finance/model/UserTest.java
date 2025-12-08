package com.university.finance.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {
    private User user;

    @Before
    public void setUp() {
        user = new User("john_doe", "password123", UserType.PREMIUM);
    }

    @Test
    public void testUserCreation() {
        assertNotNull("User ID should not be null", user.getUserId());
        assertEquals("Username should match", "john_doe", user.getUsername());
        assertEquals("User type should be PREMIUM", UserType.PREMIUM, user.getType());
    }

    @Test
    public void testAuthenticateCorrectPassword() {
        assertTrue("Authentication should succeed with correct password",
                user.authenticate("password123"));
    }

    @Test
    public void testAuthenticateWrongPassword() {
        assertFalse("Authentication should fail with wrong password",
                user.authenticate("wrongpassword"));
    }

    @Test
    public void testAuthenticateNullPassword() {
        assertFalse("Authentication should fail with null password",
                user.authenticate(null));
    }

    @Test
    public void testCreateStandardUser() {
        User standardUser = new User("standard", "pass", UserType.STANDARD);
        assertEquals("User type should be STANDARD",
                UserType.STANDARD, standardUser.getType());
    }
}
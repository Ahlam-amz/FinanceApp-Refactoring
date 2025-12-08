package com.university.finance.pattern.factory;

import com.university.finance.model.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserFactoryTest {
    private UserFactory userFactory = new UserFactory();

    @Test
    public void testCreateStandardUser() {
        User user = userFactory.createUser("john", "password", "STANDARD");

        assertNotNull("User should not be null", user);
        assertEquals("Username should match", "john", user.getUsername());
        assertEquals("User type should be STANDARD", UserType.STANDARD, user.getType());
        assertTrue("Authentication should work", user.authenticate("password"));
    }

    @Test
    public void testCreatePremiumUser() {
        User user = userFactory.createUser("jane", "secret", "PREMIUM");

        assertNotNull("User should not be null", user);
        assertEquals("Username should match", "jane", user.getUsername());
        assertEquals("User type should be PREMIUM", UserType.PREMIUM, user.getType());
        assertTrue("Authentication should work", user.authenticate("secret"));
    }

    @Test
    public void testCreateUserDefaultType() {
        User user = userFactory.createUser("bob", "pass", "UNKNOWN");

        assertNotNull("User should not be null", user);
        assertEquals("Default type should be STANDARD", UserType.STANDARD, user.getType());
    }

    @Test
    public void testCreateStandardUserDirectMethod() {
        User user = userFactory.createStandardUser("alice", "password123");

        assertNotNull("User should not be null", user);
        assertEquals("User type should be STANDARD", UserType.STANDARD, user.getType());
    }

    @Test
    public void testCreatePremiumUserDirectMethod() {
        User user = userFactory.createPremiumUser("admin", "admin123");

        assertNotNull("User should not be null", user);
        assertEquals("User type should be PREMIUM", UserType.PREMIUM, user.getType());
    }
}
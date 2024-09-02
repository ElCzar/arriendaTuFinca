package com.gossip.arrienda_tu_finca.services;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Description;

class TestPasswordEncryptionService {
    private PasswordEncryptionService passwordEncryptionService;

    @BeforeEach
    void setUp() {
        passwordEncryptionService = new PasswordEncryptionService();
    }

    @AfterEach
    void tearDown() {
        passwordEncryptionService = null;
    }

    @Test
    @Description("Test to encrypt a password and check that the encrypted password is different from the original password")
    void testEncryptPassword() {
        // Arrange
        String password = "password";
        String passwordAlternative = "password1";

        // Act
        String encryptedPassword = passwordEncryptionService.encryptPassword(password);
        String encryptedPasswordAlternative = passwordEncryptionService.encryptPassword(passwordAlternative);

        // Assert
        assertNotEquals(password, encryptedPassword);
        assertNotEquals(passwordAlternative, encryptedPasswordAlternative);
        assertNotEquals(encryptedPassword, encryptedPasswordAlternative);
    }

    @Test
    @Description("Test to check that a password before encryption when compared to the correct encrypted password via the service function returns true")
    void testCheckPasswordIsCorrect() {
        // Arrange
        String password = "password";
        String passwordAlternative = "password1";
        String encryptedPassword = passwordEncryptionService.encryptPassword(password);
        String encryptedPasswordAlternative = passwordEncryptionService.encryptPassword(passwordAlternative);

        // Act
        boolean isPasswordCorrect = passwordEncryptionService.checkPassword(password, encryptedPassword);
        boolean isPasswordAlternativeCorrect = passwordEncryptionService.checkPassword(passwordAlternative, encryptedPasswordAlternative);

        // Assert
        assert(isPasswordCorrect);
        assert(isPasswordAlternativeCorrect);
    }

    @Test
    @Description("Test to check that a password before encryption when compared to the incorrect encrypted password via the service function returns false")
    void testCheckPasswordIsIncorrect() {
        // Arrange
        String password = "password";
        String passwordAlternative = "password1";
        String encryptedPassword = passwordEncryptionService.encryptPassword(password);
        String encryptedPasswordAlternative = passwordEncryptionService.encryptPassword(passwordAlternative);

        // Act
        boolean isPasswordCorrect = passwordEncryptionService.checkPassword(password, encryptedPasswordAlternative);
        boolean isPasswordAlternativeCorrect = passwordEncryptionService.checkPassword(passwordAlternative, encryptedPassword);

        // Assert
        assert(!isPasswordCorrect);
        assert(!isPasswordAlternativeCorrect);
    }
}

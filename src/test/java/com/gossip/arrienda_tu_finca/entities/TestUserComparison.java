package com.gossip.arrienda_tu_finca.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TestUserComparison {

    @ParameterizedTest
    @MethodSource("provideUsersForEquality")
    @DisplayName("Given two users, no matter their code, when the users are equal, then the equals method returns true")
    void givenTwoUsers_whenUsersAreEqual_thenEqualsReturnsTrue(User userA, User userB) {
        // When
        boolean areEqual = userA.equals(userB);
        int code1 = userA.hashCode();
        int code2 = userB.hashCode();

        // Then
        assertTrue(areEqual);
        assertEquals(code1, code2);
    }

    private static Stream<Arguments> provideUsersForEquality() {
        return Stream.of(
            Arguments.of(
                new User(1L, "john@ex.com", "John", "Doe", "password", "123456789", true, 0D, false, 0D, 0, null),
                new User(2L, "john@ex.com", "John", "Doe", "password", "123456789", true, 0D, false, 0D, 0, null)
            ),
            Arguments.of(
                new User(2L, "jane@ex.com", "Jane", "Smith", "password123", "987654321", false, 0D. true, 0D, 0, null),
                new User(3L, "jane@ex.com", "Jane", "Smith", "password123", "987654321", false, 0D, true, 0D, 0, null)
            ),
            Arguments.of(
                new User(),
                new User()
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideUsersForInequality")
    @DisplayName("Given two users, when the users are not equal, then the equals method returns false")
    void givenTwoUsers_whenUsersAreNotEqual_thenEqualsReturnsFalse(User userA, User userB) {
        // When
        boolean areEqual = userA.equals(userB);

        // Then
        assertFalse(areEqual);
    }

    private static Stream<Arguments> provideUsersForInequality() {
        return Stream.of(
            Arguments.of(
                new User(1L, "jane@ex.com", "Jane", "Smith", "password123", "987654321", false, 0D, true, 0D, 0, null),
                new User(2L, "jane@ex.com", "Jane", "Smith", "password123", "987654321", false, 0D, true, 0D, 0, null)
            ),
            Arguments.of(
                new User(1L, "john@ex.com", "John", "Doe", "password", "123456789", true, 0D, false, 0D, 0, null),
                new User()
            )
        );
    }


}

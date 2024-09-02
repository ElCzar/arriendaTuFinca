package com.gossip.arrienda_tu_finca.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.gossip.arrienda_tu_finca.ArriendaTuFincaApplication;
import com.gossip.arrienda_tu_finca.dto.UserDTO;
import com.gossip.arrienda_tu_finca.entities.User;
import com.gossip.arrienda_tu_finca.repositories.UserRepository;

import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = ArriendaTuFincaApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(
    locations = "classpath:application-test.properties"
)
class TestUserController {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    @Transactional
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    @Description("Test the user creation where all the fields are valid")
    void givenCorrectUser_whenCreateUser_thenUserCreated() throws Exception {
        // Arrange
        // Creates a json string
        String request = 
        """
        {
            "email": "johnDoe@gmail.com",
            "name": "John",
            "surname": "Doe",
            "password": "Password123$",
            "phone": "123456789",
            "host": true,
            "renter": false
        }
        """;

        // Act
        // Perform a request to the endpoint and expect and OK status
        mvc.perform(MockMvcRequestBuilders.post("/api/user/create")
            .contentType("application/json")
            .content(request))
            .andExpect(MockMvcResultMatchers.status().isOk());
        
        // Assert
        // Check if the only user was created  
        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());   
        User user = userRepository.findByEmail("johnDoe@gmail.com");
        assertNotNull(user);
        assertEquals("John", user.getName());
        assertEquals("Doe", user.getSurname());
        assertNotEquals("Password123$", user.getPassword());
        assertEquals("123456789", user.getPhone());
        assertEquals(true, user.isHost());
        assertEquals(false, user.isRenter());
        assertEquals("johnDoe@gmail.com", user.getEmail());
    }

    @ParameterizedTest
    @Transactional
    @MethodSource("provideNotValidUsers")
    @Description("Test the user creation where different invalid users are provided")
    void givenInvalidUsers_whenCreateUser_thenUserNotCreated(String jsonPayload) throws Exception {
        // Arrange
        // Creates a json string
        String request = jsonPayload;

        // Act
        // Perform a request to the endpoint and expect a bad request status
        mvc.perform(MockMvcRequestBuilders.post("/api/user/create")
            .contentType("application/json")
            .content(request))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
        
        // Assert
        // Check if there is any user created on the database
        List<User> users = userRepository.findAll();
        assertEquals(0, users.size());
    }

    private static Stream<String> provideNotValidUsers() {
        return Stream.of(
        """
        {
            "email": null,
            "name": "John",
            "surname": "Doe",
            "password": "Password123$",
            "phone": "123456789",
            "host": true,
            "renter": false
        }
        """,
        """
        {
            "email": "johnDoe@gmail.com",
            "name": null,
            "surname": null,
            "password": null,
            "phone": null,
            "host": false,
            "renter": false
        }
        """,
        """
        {
            "email": "johnDoe@gmail.com",
            "name": "John",
            "surname": "Doe",
            "password": "password123",
            "phone": "123456789",
            "host": true,
            "renter": false
        }
        """,
        """
        {
        
        }
        """,
        """
        {
            "email": "johnDoe@gmail.com",
            "password": "password123"
        }
        """,
        """
        {
            "email": "johnDoe-gmailcom",
            "name": "John",
            "surname": "Doe",
            "password": "Password123$",
            "phone": "123456789",
            "host": true,
            "renter": false,
            "extraInformation": "extra"
        }
        """
        );
    }

    @Test
    @Transactional
    @Description("Test the user creation where the email is already in use")
    void givenEmailInUse_whenCreateUser_thenUserNotCreated() throws Exception {
        // Arrange
        // Creates a json string
        String request = 
        """
        {
            "email": "johnDoe@gmail.com",
            "name": "John",
            "surname": "Doe",
            "password": "Password123$",
            "phone": "123456789",
            "host": true,
            "renter": false
        }
        """;
        // Create a user with the same email
        String requestRepeated = 
        """
        {
            "email": "johnDoe@gmail.com",
            "name": "NotJohn",
            "surname": "Doent",
            "password": "Password123$",
            "phone": "987654321",
            "host": false,
            "renter": true
        }
        """;

        // Act
        // Perform a request to the endpoint and expect an OK status
        mvc.perform(MockMvcRequestBuilders.post("/api/user/create")
            .contentType("application/json")
            .content(request))
            .andExpect(MockMvcResultMatchers.status().isOk());
        
        // Perform a request to the endpoint and expect a bad request status
        mvc.perform(MockMvcRequestBuilders.post("/api/user/create")
            .contentType("application/json")
            .content(requestRepeated))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Assert
        // Check if there is only one user created on the database
        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());
        User userNotRepeated = userRepository.findByEmail("johnDoe@gmail.com");
        assertNotNull(userNotRepeated);
        assertEquals("John", userNotRepeated.getName());
        assertEquals("Doe", userNotRepeated.getSurname());
        assertNotEquals("Password123$", userNotRepeated.getPassword());
        assertEquals("123456789", userNotRepeated.getPhone());
        assertEquals(true, userNotRepeated.isHost());
        assertEquals(false, userNotRepeated.isRenter());
    }

    @Test
    @Transactional
    @Description("Test the user creation is giving out different user ids")
    void givenDifferentUsers_whenCreateUser_thenDifferentUserIds() throws Exception {
        // Arrange
        // Creates a json string
        String request = 
        """
        {
            "email": "johnDoe@gmail.com",
            "name": "John",
            "surname": "Doe",
            "password": "Password123$",
            "phone": "123456789",
            "host": true,
            "renter": false
        }
        """;
        // Create a user with different email
        String requestDifferent = 
        """
        {
            "email": "johnNotDoe@gmail.com",
            "name": "NotJohn",
            "surname": "Doent",
            "password": "Password123$",
            "phone": "987654321",
            "host": false,
            "renter": true
        }
        """;

        // Act
        // Perform a request to the endpoint and expect an OK status
        mvc.perform(MockMvcRequestBuilders.post("/api/user/create")
            .contentType("application/json")
            .content(request))
            .andExpect(MockMvcResultMatchers.status().isOk());
        mvc.perform(MockMvcRequestBuilders.post("/api/user/create")
            .contentType("application/json")
            .content(requestDifferent))
            .andExpect(MockMvcResultMatchers.status().isOk());
        
        // Assert
        // Check if there are two users created on the database
        List<User> users = userRepository.findAll();
        assertEquals(2, users.size());
        User userOriginal = userRepository.findByEmail("johnDoe@gmail.com");
        assertNotNull(userOriginal);
        assertEquals("John", userOriginal.getName());
        assertEquals("Doe", userOriginal.getSurname());
        assertNotEquals("Password123$", userOriginal.getPassword());
        assertEquals("123456789", userOriginal.getPhone());
        assertEquals(true, userOriginal.isHost());
        assertEquals(false, userOriginal.isRenter());
        User userDifferent = userRepository.findByEmail("johnNotDoe@gmail.com");
        assertNotNull(userDifferent);
        assertEquals("NotJohn", userDifferent.getName());
        assertEquals("Doent", userDifferent.getSurname());
        assertNotEquals("Password123$", userDifferent.getPassword());
        assertEquals("987654321", userDifferent.getPhone());
        assertEquals(false, userDifferent.isHost());
        assertEquals(true, userDifferent.isRenter());
        assertNotEquals(userOriginal.getId(), userDifferent.getId());
    }
} 
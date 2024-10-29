package com.gossip.arrienda_tu_finca.controllers;

import com.gossip.arrienda_tu_finca.ArriendaTuFincaApplication;
import com.gossip.arrienda_tu_finca.entities.Comment;
import com.gossip.arrienda_tu_finca.entities.Property;
import com.gossip.arrienda_tu_finca.entities.RentalRequest;
import com.gossip.arrienda_tu_finca.entities.User;
import com.gossip.arrienda_tu_finca.repositories.PropertyRepository;
import com.gossip.arrienda_tu_finca.repositories.RentalRequestRepository;
import com.gossip.arrienda_tu_finca.repositories.UserRepository;
import com.gossip.arrienda_tu_finca.services.RentalRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = ArriendaTuFincaApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(
    locations = "classpath:application-test.properties"
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class TestRentalRequestController {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RentalRequestRepository rentalRequestRepository;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    @Transactional
    void setUp() {
        rentalRequestRepository.deleteAllInBatch();
        propertyRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        // Creates two users
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("host@example.com");
        user1.setPassword("password");
        user1.setName("Host");
        user1.setSurname("User");
        user1.setPhone("1234567890");
        user1.setHost(true);
        user1.setRenter(false);

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("renter@example.com");
        user2.setPassword("password");
        user2.setName("Renter");
        user2.setSurname("User");
        user2.setPhone("1234567890");
        user2.setHost(false);
        user2.setRenter(true);

        userRepository.save(user1);
        userRepository.save(user2);
        
        // Creates a property
        Property property = new Property();
        property.setId(1L);
        property.setName("Property");
        property.setDescription("Description");
        property.setPricePerNight(1000.D);
        property.setAmountOfResidents(10);
        property.setAmountOfRooms(5);
        property.setAmountOfBathrooms(3);
        property.setOwner(user1);
        propertyRepository.save(property);
    }

    @Test
    @DirtiesContext
    @Transactional
    @Description("Test the rental request creation")
    void testCreateRequest() throws Exception {
        // Creates JSON string with the rental request
        String json = """
            {
                "id": 1,
                "propertyId": 1,
                "requesterEmail": "renter@example.com",
                "requestDateTime": "2024-10-28T15:30:00",
                "arrivalDate": "2024-11-01",
                "departureDate": "2024-11-10",
                "amountOfResidents": 4,
                "amount": 250.75,
                "rejected": false,
                "canceled": false,
                "paid": false,
                "completed": false,
                "approved": false,
                "expired": false,
                "bank": "Example Bank",
                "accountNumber": 123456789
            }
            """;

        // Act
        mvc.perform(post("/rental-requests/create/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk());

        // Assert
        List<RentalRequest> rentalRequests = rentalRequestRepository.findAll();
        assertEquals(1, rentalRequests.size());
    }

    @Test
    @DirtiesContext
    @Transactional
    @Description("Test getting all rental requests by host")
    void testGetRequestsByHost() throws Exception {
        // Creates a rental request
        RentalRequest rentalRequest = new RentalRequest();
        rentalRequest.setId(1L);
        rentalRequest.setProperty(propertyRepository.findById(1L).get());
        rentalRequest.setRequester(userRepository.findById(2L).get());
        rentalRequest.setAmount(250.75);
        rentalRequest.setAmountOfResidents(4);
        rentalRequest.setBank("Example Bank");
        rentalRequest.setAccountNumber(123456789);
        rentalRequestRepository.save(rentalRequest);

        String email = "host@example.com";

        // Act
        ResultActions resultActions = mvc.perform(get("/rental-requests/host")
            .contentType(MediaType.APPLICATION_JSON)
            .content(email));
        
        // Assert
        resultActions.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].propertyId").value(1))
            .andExpect(jsonPath("$[0].requesterEmail").value("renter@example.com"));
    }

    
}
package com.gossip.arrienda_tu_finca.controllers;

import com.gossip.arrienda_tu_finca.ArriendaTuFincaApplication;
import com.gossip.arrienda_tu_finca.entities.Property;
import com.gossip.arrienda_tu_finca.entities.RentalRequest;
import com.gossip.arrienda_tu_finca.entities.User;
import com.gossip.arrienda_tu_finca.repositories.PropertyRepository;
import com.gossip.arrienda_tu_finca.repositories.RentalRequestRepository;
import com.gossip.arrienda_tu_finca.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import jakarta.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
                "requesterEmail": "renter@example.com",
                "arrivalDate": "2024-11-01",
                "departureDate": "2024-11-10",
                "amountOfResidents": 4
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

    private Long createNewRentalRequest() {
        RentalRequest rentalRequest = new RentalRequest();
        rentalRequest.setId(1L);
        rentalRequest.setProperty(propertyRepository.findById(1L).get());
        rentalRequest.setRequester(userRepository.findById(2L).get());
        rentalRequest.setAmount(250.75);
        rentalRequest.setAmountOfResidents(4);
        rentalRequest.setBank("Example Bank");
        rentalRequest.setAccountNumber(123456789);
        return rentalRequestRepository.save(rentalRequest).getId();
    }

    @Test
    @DirtiesContext
    @Transactional
    @Description("Test getting all rental requests by host")
    void testGetRequestsByHost() throws Exception {
        // Creates a rental request
        createNewRentalRequest();
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

    @Test
    @DirtiesContext
    @Transactional
    @Description("Test getting all rental requests by renter")
    void testGetRequestsByRenter() throws Exception {
        // Creates a rental request
        createNewRentalRequest();
        String email = "renter@example.com";
        
        // Act
        ResultActions resultActions = mvc.perform(get("/rental-requests/renter")
            .contentType(MediaType.APPLICATION_JSON)
            .content(email));

        // Assert
        resultActions.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].propertyId").value(1))
            .andExpect(jsonPath("$[0].requesterEmail").value("renter@example.com"));
    }

    @Test
    @DirtiesContext
    @Transactional
    @Description("Test getting all rental requests by property")
    void testGetRequestsByProperty() throws Exception{
        // Creates a rental request
        createNewRentalRequest();

        // Act
        ResultActions resultActions = mvc.perform(get("/rental-requests/property/1")
            .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].propertyId").value(1))
            .andExpect(jsonPath("$[0].requesterEmail").value("renter@example.com"));
    }


    @Test
    @DirtiesContext
    @Transactional
    @Description("Test cancelling a rental request")
    void testCancelRequest() throws Exception {
        // Creates a rental request
        Long id = createNewRentalRequest();

        // Act
        mvc.perform(put("/rental-requests/cancel/" + id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        // Assert
        List<RentalRequest> rentalRequests = rentalRequestRepository.findAll();
        assertEquals(1, rentalRequests.size());
        assertEquals(true, rentalRequests.get(0).isCanceled());
    }
    
    @Test
    @DirtiesContext
    @Transactional
    @Description("Test completing a rental request")
    void testCompleteRequest() throws Exception {
        // Creates a rental request
        Long id = createNewRentalRequest();

        // Act
        mvc.perform(put("/rental-requests/complete/" + id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        // Assert
        List<RentalRequest> rentalRequests = rentalRequestRepository.findAll();
        assertEquals(1, rentalRequests.size());
        assertEquals(true, rentalRequests.get(0).isCompleted());
    }

    @Test
    @DirtiesContext
    @Transactional
    @Description("Test rejecting a rental request")
    void testRejectRequest() throws Exception {
        // Creates a rental request
        Long id = createNewRentalRequest();

        // Act
        mvc.perform(put("/rental-requests/reject/" + id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        // Assert
        List<RentalRequest> rentalRequests = rentalRequestRepository.findAll();
        assertEquals(1, rentalRequests.size());
        assertEquals(true, rentalRequests.get(0).isRejected());
    }

    @Test
    @DirtiesContext
    @Transactional
    @Description("Test approving a rental request")
    void testApproveRequest() throws Exception {
        // Creates a rental request
        Long id = createNewRentalRequest();

        // Act
        mvc.perform(put("/rental-requests/approve/" + id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        // Assert
        List<RentalRequest> rentalRequests = rentalRequestRepository.findAll();
        assertEquals(1, rentalRequests.size());
        assertEquals(true, rentalRequests.get(0).isApproved());
    }

    @Test
    @DirtiesContext
    @Transactional
    @Description("Test paying a rental request")
    void testPayRequest() throws Exception {
        // Creates a rental request
        Long id = createNewRentalRequest();
        RentalRequest rentalRequest = rentalRequestRepository.findById(id).get();
        rentalRequest.setApproved(true);
        rentalRequestRepository.save(rentalRequest);

        // Act
        mvc.perform(put("/rental-requests/pay/" + id)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        // Assert
        List<RentalRequest> rentalRequests = rentalRequestRepository.findAll();
        assertEquals(1, rentalRequests.size());
        assertEquals(true, rentalRequests.get(0).isPaid());
    }
}
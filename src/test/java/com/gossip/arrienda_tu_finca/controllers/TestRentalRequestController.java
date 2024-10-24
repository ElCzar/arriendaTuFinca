package com.gossip.arrienda_tu_finca.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.gossip.arrienda_tu_finca.ArriendaTuFincaApplication;
import com.gossip.arrienda_tu_finca.entities.Property;
import com.gossip.arrienda_tu_finca.entities.RentalRequest;
import com.gossip.arrienda_tu_finca.entities.User;
import com.gossip.arrienda_tu_finca.repositories.PropertyRepository;
import com.gossip.arrienda_tu_finca.repositories.RentalRequestRepository;
import com.gossip.arrienda_tu_finca.repositories.UserRepository;
import com.gossip.arrienda_tu_finca.services.RentalRequestService;

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
    private MockMvc mockMvc;
    @Autowired
    private RentalRequestRepository rentalRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PropertyRepository propertyRepository;

    @BeforeEach
    void setUp() {
        rentalRequestRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        propertyRepository.deleteAllInBatch();
    }

    @Test
    void testCreateBasicRequest() {
        assertEquals("hola", "hola");
    }
}
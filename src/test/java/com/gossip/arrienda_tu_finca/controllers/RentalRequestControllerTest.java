package com.gossip.arrienda_tu_finca.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.gossip.arrienda_tu_finca.entities.RentalRequest;
import com.gossip.arrienda_tu_finca.entities.Property;
import com.gossip.arrienda_tu_finca.entities.User;
import com.gossip.arrienda_tu_finca.services.RentalRequestService;

@WebMvcTest(RentalRequestController.class)
class RentalRequestControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private RentalRequestService rentalRequestService;

    @InjectMocks
    private RentalRequestController rentalRequestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(rentalRequestController).build();
    }

    @Test
    void testGetRequestsByOwner() throws Exception {
        Property property = new Property(); 
        User requester = new User();
        requester.setEmail("test@example.com");

        RentalRequest rentalRequest = new RentalRequest(1L, property, requester, LocalDateTime.now(), LocalDate.now(), LocalDate.now().plusDays(1), 100.0, false, false, false, false, false, false, false);
        List<RentalRequest> requests = Arrays.asList(rentalRequest);

        when(rentalRequestService.getRequestsByOwner("test@example.com")).thenReturn(requests);

        mockMvc.perform(get("/rental-requests/owner/test@example.com"))
            .andExpect(status().isOk())
            .andExpect(content().json("[{'id':1,'propertyId':1,'requesterEmail':'test@example.com','amount':100.0}]"));
    }
}
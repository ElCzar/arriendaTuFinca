package com.gossip.arrienda_tu_finca.controllers;

import com.gossip.arrienda_tu_finca.entities.Property;
import com.gossip.arrienda_tu_finca.entities.RentalRequest;
import com.gossip.arrienda_tu_finca.entities.User;
import com.gossip.arrienda_tu_finca.dto.RentalRequestDto;
import com.gossip.arrienda_tu_finca.services.RentalRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

 class RentalRequestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RentalRequestService rentalRequestService;

    @InjectMocks
    private RentalRequestController rentalRequestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(rentalRequestController).build();
    }

        @Test
    void testGetRequestsByOwner() throws Exception {
        String email = "owner@example.com";
        
        // Crear una propiedad y asignarla a RentalRequest
        Property property = new Property();
        property.setId(1L); // Asignar un ID a la propiedad
        
        // Crear un usuario y asignarlo a RentalRequest
        User requester = new User();
        requester.setEmail("requester@example.com"); // Asignar un email al usuario
        
        RentalRequest rentalRequest = new RentalRequest();
        rentalRequest.setProperty(property); // Asignar la propiedad a RentalRequest
        rentalRequest.setRequester(requester); // Asignar el usuario a RentalRequest
        
        List<RentalRequest> rentalRequests = Arrays.asList(rentalRequest);
    
        // Mock del servicio para devolver una lista de RentalRequest
        when(rentalRequestService.getRequestsByOwner(email)).thenReturn(rentalRequests);
    
        mockMvc.perform(get("/rental-requests/owner/{email}", email))
               .andExpect(status().isOk());
    }
    

    @Test
    void testAcceptRequest() throws Exception {
        Long requestId = 1L;

        mockMvc.perform(put("/rental-requests/{requestId}/accept", requestId))
                .andExpect(status().isOk());

        ResponseEntity<String> response = rentalRequestController.acceptRequest(requestId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Solicitud de arriendo aceptada", response.getBody());
    }

    @Test
    void testCancelRequest() throws Exception {
        Long requestId = 1L;

        mockMvc.perform(put("/rental-requests/{requestId}/cancel", requestId))
                .andExpect(status().isOk());

        ResponseEntity<String> response = rentalRequestController.cancelRequest(requestId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Solicitud de arriendo cancelada", response.getBody());
    }

    @Test
    void testReviewRenter() throws Exception {
        Long requestId = 1L;

        mockMvc.perform(put("/rental-requests/{requestId}/review", requestId))
                .andExpect(status().isOk());

        ResponseEntity<String> response = rentalRequestController.reviewRenter(requestId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Arrendatario calificado", response.getBody());
    }

    @Test
    void testCompleteRequest() throws Exception {
        Long requestId = 1L;

        mockMvc.perform(put("/rental-requests/{requestId}/complete", requestId))
                .andExpect(status().isOk());

        ResponseEntity<String> response = rentalRequestController.completeRequest(requestId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Solicitud de arriendo completada", response.getBody());
    }

    @Test
    void testRejectRequest() throws Exception {
        Long requestId = 1L;

        mockMvc.perform(put("/rental-requests/{requestId}/reject", requestId))
                .andExpect(status().isOk());

        ResponseEntity<String> response = rentalRequestController.rejectRequest(requestId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Solicitud de arriendo rechazada", response.getBody());
    }

    @Test
    void testApproveRequest() throws Exception {
        Long requestId = 1L;

        mockMvc.perform(put("/rental-requests/{requestId}/approve", requestId))
                .andExpect(status().isOk());

        ResponseEntity<String> response = rentalRequestController.approveRequest(requestId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Solicitud de arriendo aprobada", response.getBody());
    }

    @Test
    void testPayRequest() throws Exception {
        Long requestId = 1L;

        mockMvc.perform(put("/rental-requests/{requestId}/pay", requestId))
                .andExpect(status().isOk());

        ResponseEntity<String> response = rentalRequestController.payRequest(requestId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Solicitud de arriendo pagada", response.getBody());
    }



}

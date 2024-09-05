package com.gossip.arrienda_tu_finca.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.naming.java.javaURLContextFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.gossip.arrienda_tu_finca.dto.TenantRentalRequestDTO;
import com.gossip.arrienda_tu_finca.entities.Property;
import com.gossip.arrienda_tu_finca.entities.TenantRentalRequest;
import com.gossip.arrienda_tu_finca.entities.User;
import com.gossip.arrienda_tu_finca.exceptions.RentalRequestNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gossip.arrienda_tu_finca.dto.RequestARentalDTO;
import com.gossip.arrienda_tu_finca.services.TenantRentalRequestService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content; // Para usar content()

@SpringBootTest
@AutoConfigureMockMvc
public class TenantRentalRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TenantRentalRequestService tenantRentalRequestService;

    // Test No. 1: getAllRentalsRequests

    @Test // Test 1.1. Exito: Mostrar las solicitudes de renta del arrendatario
    public void testGetAllRentalRequestsSuccess() throws Exception {
        String email = "katyperry@gmail.com";
        List<TenantRentalRequestDTO> rentalRequests = Arrays.asList(
            new TenantRentalRequestDTO(1L, 2L, email, LocalDateTime.now(), LocalDate.now(), LocalDate.now().plusDays(2), 4, 800000.0, true, false, false, true, false, false, true, true)
        );
    
        Mockito.when(tenantRentalRequestService.getAllRequestsByTenant(email)).thenReturn(rentalRequests);
    
        mockMvc.perform(MockMvcRequestBuilders.get("/tenant")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1)) 
                .andExpect(jsonPath("$[0].requesterEmail").value(email));
    }

    @Test // Test 1.2. Fallo: Lanzar una excepción cuando no se encontraron solicitudes de renta del arrendatario
    public void testGetAllRentalsRequestsFailure() throws Exception {
        String email = "javeriana@gmail.com";
        
        Mockito.when(tenantRentalRequestService.getAllRequestsByTenant(email))
               .thenThrow(new RentalRequestNotFoundException("Rental request not found"));
        
        mockMvc.perform(MockMvcRequestBuilders.get("/tenant")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());  
    }

    // Test No. 2: updateRentalRequest

    @Test // Test 2.1. Exito: Actualizar los datos de una solicitud de renta
    public void testUpdateRentalRequestSuccess() throws Exception {
        Long requestId = 1L;
        RequestARentalDTO requestDTO = new RequestARentalDTO(1L, 2L, "katyperry@gmail.com", LocalDate.now(), LocalDate.now().plusDays(2), 4);
        TenantRentalRequest updatedRequest = new TenantRentalRequest(1L, new Property(), new User(), LocalDateTime.now(), LocalDate.now(), LocalDate.now().plusDays(2), 4, 800000.0, true, false, false, true, false, false, true, true);
    
        Mockito.when(tenantRentalRequestService.updateRentalRequest(requestId, requestDTO)).thenReturn(updatedRequest);
    
        mockMvc.perform(MockMvcRequestBuilders.put("/tenant/{id}", requestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.peopleNumber").value(4));  
    }

    @Test // Test 2.2. Fallo: Lanzar una excepción cuando no se encuentra una solicitud de renta con el ID del arrendatario
    public void testUpdateRentalRequestFailure() throws Exception {
        Long requestId = 99L;
        RequestARentalDTO requestDTO = new RequestARentalDTO(99L, 2L, "katyperry@gmail.com", LocalDate.now(), LocalDate.now().plusDays(2), 4);
    
        Mockito.when(tenantRentalRequestService.updateRentalRequest(requestId, requestDTO))
               .thenThrow(new RentalRequestNotFoundException("Rental request not found"));
    
        mockMvc.perform(MockMvcRequestBuilders.put("/tenant/{id}", requestId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());  
    }

    // Test No. 3: reviewProperty

    @Test // Test 3.1. Exito: Calificar correctamente una propiedad
    public void testReviewPropertySuccess() throws Exception {
        Long requestId = 1L;
    
        Mockito.doNothing().when(tenantRentalRequestService).reviewProperty(requestId);
    
        mockMvc.perform(MockMvcRequestBuilders.put("/tenant/{requestId}/propertyReview", requestId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Propiedad calificada"));  // Verifica que se muestra el mensaje esperado
    }

    @Test // Test 3.2. Fallo: Lanzar una excepción cuando no se encuentra la solicitud de arrendamiento al intentar calificar una propiedad
    public void testReviewPropertyFailure() throws Exception {
        Long requestId = 99L;
    
        Mockito.doThrow(new RentalRequestNotFoundException("Rental request not found"))
               .when(tenantRentalRequestService).reviewProperty(requestId);
    
        mockMvc.perform(MockMvcRequestBuilders.put("/tenant/{requestId}/propertyReview", requestId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // Test No. 4: reviewLandlord

    @Test // Test 4.1. Exito: Calificar correctamente un arrendador
    public void testReviewLandlordSuccess() throws Exception {
        Long requestId = 1L;
    
        Mockito.doNothing().when(tenantRentalRequestService).reviewLandlord(requestId);
    
        mockMvc.perform(MockMvcRequestBuilders.put("/tenant/{requestId}/landlordReview", requestId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Arrendador calificado")); 
    }

    @Test  // Test 4.2. Fallo: Lanzar una excepción cuando no se encuentra la solicitud de arrendamiento al intentar calificar un arrendador
    public void testReviewLandlordFailure() throws Exception {
        Long requestId = 99L;
    
        Mockito.doThrow(new RentalRequestNotFoundException("Rental request not found"))
               .when(tenantRentalRequestService).reviewLandlord(requestId);
    
        mockMvc.perform(MockMvcRequestBuilders.put("/tenant/{requestId}/landlordReview", requestId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

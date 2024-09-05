package com.gossip.arrienda_tu_finca.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gossip.arrienda_tu_finca.dto.PaymentDTO;
import com.gossip.arrienda_tu_finca.entities.Payment;
import com.gossip.arrienda_tu_finca.entities.TenantRentalRequest;
import com.gossip.arrienda_tu_finca.exceptions.PaymentNotFoundException;
import com.gossip.arrienda_tu_finca.services.PaymentService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.springframework.http.MediaType;


@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Test No. 1: getRentalPrice

    @Test // Test 1.1. Exito: Obtener correctamente el precio de renta de un Payment
    public void testGetRentalPriceSuccess() throws Exception {
        Long paymentId = 1L;
        Double rentalPrice = 800000.0;
    
        when(paymentService.getRentalPrice(paymentId)).thenReturn(rentalPrice);
    
        mockMvc.perform(get("/payment/{id}/rentalPrice", paymentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(rentalPrice));
    
        verify(paymentService, times(1)).getRentalPrice(paymentId);
    }

    @Test // Test 1.2. Fallo: Lanzar una excepción cuando no hay un precio de venta
    public void testGetRentalPriceNotFound() throws Exception {
        Long paymentId = 1L;
    
        when(paymentService.getRentalPrice(paymentId)).thenThrow(new PaymentNotFoundException("Payment not found"));
    
        mockMvc.perform(get("/payment/{id}/rentalPrice", paymentId))
                .andExpect(status().isNotFound());
    
        verify(paymentService, times(1)).getRentalPrice(paymentId);
    }

    // Test No. 2: updatePayment

    @Test // Test 2.1. Exito: Comprobar que un pago se actualiza correctamente.
    public void testUpdatePaymentSuccessWithRentalRequest() throws Exception {
        Long paymentId = 1L;
        PaymentDTO paymentDTO = new PaymentDTO(1L, 1L, 800000.0, "Bancolombia", 143143143);
    
        TenantRentalRequest rentalRequest = new TenantRentalRequest(); 
        rentalRequest.setId(1L);
        
        Payment updatedPayment = new Payment(1L, 800000.0, "Bancolombia", 123456789, rentalRequest);
    
        when(paymentService.updatePayment(eq(paymentId), any(PaymentDTO.class))).thenReturn(updatedPayment);
    
        mockMvc.perform(put("/payment/{id}", paymentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(paymentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bank").value("Bancolombia"))
                .andExpect(jsonPath("$.accountNumber").value(123456789))
                .andExpect(jsonPath("$.rentalRequest.id").value(1L));  
    
        verify(paymentService, times(1)).updatePayment(eq(paymentId), any(PaymentDTO.class));
    }
    
    @Test // Test 2.2. Fallo: Lanzar una excepción cuando no se encuentra el Payment a actualizar
    public void testUpdatePaymentNotFound() throws Exception {
        Long paymentId = 1L;
        PaymentDTO paymentDTO = new PaymentDTO(1L, 1L, 800000.0, "Bancolombia", 143143143);
    
        when(paymentService.updatePayment(eq(paymentId), any(PaymentDTO.class))).thenThrow(new PaymentNotFoundException("Payment not found"));
    
        mockMvc.perform(put("/payment/{id}", paymentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(paymentDTO)))
                .andExpect(status().isNotFound());
    
        verify(paymentService, times(1)).updatePayment(eq(paymentId), any(PaymentDTO.class));
    }

    // Test No. 3: getAllBanks

    @Test // Test 3.1: Obtener 
    public void testGetAllBanksSuccess() throws Exception {
        List<String> banks = List.of("Banco de Bogotá", "Bancolombia", "BBVA");
    
        when(paymentService.getAllBanks()).thenReturn(banks);
    
        mockMvc.perform(get("/payment/banks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Banco de Bogotá"))
                .andExpect(jsonPath("$[1]").value("Bancolombia"))
                .andExpect(jsonPath("$[2]").value("BBVA"));
    
        verify(paymentService, times(1)).getAllBanks();
    }

    // Test No. 4: getPaymentByRentalRequestId

    @Test // Test 4.1. Exito: Obtener el Payment asociado a un TenantRentalRequest
    public void testGetPaymentByRentalRequestIdSuccess() throws Exception {
        Long rentalRequestId = 1L;

        TenantRentalRequest rentalRequest = new TenantRentalRequest(); 
        rentalRequest.setId(1L);

        Payment payment = new Payment(1L, 800000.0, "Bancolombia", 143143143, rentalRequest);
    
        when(paymentService.getPaymentByRentalRequestId(rentalRequestId)).thenReturn(payment);
    
        mockMvc.perform(get("/payment/rentalRequest/{rentalRequestId}", rentalRequestId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bank").value("Bancolombia"))
                .andExpect(jsonPath("$.accountNumber").value(143143143));
    
        verify(paymentService, times(1)).getPaymentByRentalRequestId(rentalRequestId);
    }

    @Test  // Test 4.2. Fallo: Lanzar una excepción cuando no se encuentra el Payment asociado a un TenantRentalRequest
    public void testGetPaymentByRentalRequestIdNotFound() throws Exception {
        Long rentalRequestId = 1L;
    
        when(paymentService.getPaymentByRentalRequestId(rentalRequestId)).thenThrow(new PaymentNotFoundException("Payment not found"));
    
        mockMvc.perform(get("/payment/rentalRequest/{rentalRequestId}", rentalRequestId))
                .andExpect(status().isNotFound());
    
        verify(paymentService, times(1)).getPaymentByRentalRequestId(rentalRequestId);
    }







    
   
    













}

package com.gossip.arrienda_tu_finca.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gossip.arrienda_tu_finca.dto.PaymentDTO;
import com.gossip.arrienda_tu_finca.entities.Payment;
import com.gossip.arrienda_tu_finca.entities.TenantRentalRequest;
import com.gossip.arrienda_tu_finca.exceptions.PaymentNotFoundException;
import com.gossip.arrienda_tu_finca.repositories.PaymentRepository;
import com.gossip.arrienda_tu_finca.services.PaymentService;

class PaymentControllerTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test No. 1: getRentalPrice

    @Test // Test 1.1. Exito: Obtener correctamente el precio de renta de un Payment
    void testGetRentalPrice() { 
        Payment payment = new Payment();
        payment.setRentalPrice(100.0);

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        Double rentalPrice = paymentService.getRentalPrice(1L);

        assertEquals(100.0, rentalPrice);
    }

    @Test // Test 1.2. Fallo: Lanzar una excepción cuando no hay un precio de venta
    void testGetRentalPrice_NotFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class, () -> paymentService.getRentalPrice(1L));
    }
    
    // Test No. 2: updatePayment

    @Test // Test 2.1. Exito: Comprobar que un pago se actualiza correctamente.
    void testUpdatePayment() {
     
        Payment payment = new Payment();
        payment.setRentalPrice(900000.0);
        TenantRentalRequest rentalRequest = new TenantRentalRequest();
        payment.setRentalRequest(rentalRequest);
   
        PaymentDTO paymentDTO = new PaymentDTO(1L, 800000.0, "Banco de Bogotá", 143143143, 1L);
    
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(payment)).thenReturn(payment); 
    
        Payment updatedPayment = paymentService.updatePayment(1L, paymentDTO);
    
        assertEquals(900000.0, updatedPayment.getRentalPrice());
        assertEquals("Banco de Bogotá", updatedPayment.getBank());
        assertEquals(143143143, updatedPayment.getAccountNumber());
        assertTrue(updatedPayment.getRentalRequest().isPaid()); 
    }

    @Test // Test 2.2. Fallo: Lanzar una excepción cuando no se encuentra el Payment a actualizar
    void testUpdatePayment_NotFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        PaymentDTO paymentDTO = new PaymentDTO(1L, 800000.0, "Banco de Bogotá", 143143143, 1L);

        assertThrows(PaymentNotFoundException.class, () -> paymentService.updatePayment(1L, paymentDTO));
    }

      // Test No. 3: getAllBanks

    @Test // Test 3.1: Obtener correctamente la lista de bancos 
    void testGetAllBanks() {
        assertEquals(8, paymentService.getAllBanks().size());
        assertTrue(paymentService.getAllBanks().contains("Banco de Bogotá"));
    }

     // Test No. 4: getPaymentByRentalRequestId

    @Test // Test 4.1. Exito: Obtener el Payment asociado a un TenantRentalRequest
    void testGetPaymentByRentalRequestId() {
        Payment payment = new Payment();
        when(paymentRepository.findByRentalRequestId(1L)).thenReturn(Optional.of(payment));

        Payment foundPayment = paymentService.getPaymentByRentalRequestId(1L);

        assertNotNull(foundPayment);
    }

    @Test // Test 4.2. Fallo: Lanzar una excepción cuando no se encuentra el Payment asociado a un TenantRentalRequest
    void testGetPaymentByRentalRequestId_NotFound() {
        when(paymentRepository.findByRentalRequestId(1L)).thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class, () -> paymentService.getPaymentByRentalRequestId(1L));
    }
}
package com.gossip.arrienda_tu_finca.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.gossip.arrienda_tu_finca.dto.RequestARentalDTO;
import com.gossip.arrienda_tu_finca.dto.TenantRentalRequestDTO;
import com.gossip.arrienda_tu_finca.entities.TenantRentalRequest;
import com.gossip.arrienda_tu_finca.entities.Property;
import com.gossip.arrienda_tu_finca.exceptions.InvalidEndDateException;
import com.gossip.arrienda_tu_finca.exceptions.InvalidStartDateException;
import com.gossip.arrienda_tu_finca.exceptions.RentalRequestNotFoundException;
import com.gossip.arrienda_tu_finca.repositories.TenantRentalRequestRepository;
import com.gossip.arrienda_tu_finca.services.TenantRentalRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class TenantRentalRequestControllerTest {

    @Mock
    private TenantRentalRequestRepository rentalRequestRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TenantRentalRequestService tenantRentalRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test No. 1: getAllRentalsRequests

    @Test // Test 1.1. Exito: Mostrar las solicitudes de renta del arrendatario
    void testGetAllRequestsByTenantSuccess() {
        String email = "tenant@example.com";
        TenantRentalRequest rentalRequest = new TenantRentalRequest();
        when(rentalRequestRepository.findAllRentalRequestsByEmail(email)).thenReturn(List.of(rentalRequest));

        List<TenantRentalRequestDTO> result = tenantRentalRequestService.getAllRequestsByTenant(email);

        assertFalse(result.isEmpty());
        verify(rentalRequestRepository, times(1)).findAllRentalRequestsByEmail(email);
    }

    @Test // Test 1.2. Fallo: Lanzar una excepción cuando no se encontraron solicitudes de renta del arrendatario
    void testGetAllRequestsByTenantNotFound() {
        String email = "tenant@example.com";
        when(rentalRequestRepository.findAllRentalRequestsByEmail(email)).thenReturn(List.of());

        assertThrows(RentalRequestNotFoundException.class, () -> tenantRentalRequestService.getAllRequestsByTenant(email));
    }

    // Test No. 2: updateRentalRequest

    @Test // Test 2.1. Exito: Actualizar los datos de una solicitud de renta
    void testUpdateRentalRequestSuccess() {
        Long id = 1L;
        RequestARentalDTO requestDTO = new RequestARentalDTO();
        requestDTO.setStartDate(LocalDate.now().plusDays(1));
        requestDTO.setEndDate(LocalDate.now().plusDays(3));
        requestDTO.setPeopleNumber(4);

        Property property = new Property();
        property.setPeopleNumber(5); 

        TenantRentalRequest rentalRequest = new TenantRentalRequest();
        rentalRequest.setProperty(property);

        when(rentalRequestRepository.findById(id)).thenReturn(Optional.of(rentalRequest));

        tenantRentalRequestService.updateRentalRequest(id, requestDTO);

        verify(rentalRequestRepository, times(1)).save(rentalRequest);

        assertEquals(requestDTO.getStartDate(), rentalRequest.getArrivalDate());
        assertEquals(requestDTO.getEndDate(), rentalRequest.getDepartureDate());
        assertEquals(requestDTO.getPeopleNumber(), rentalRequest.getPeopleNumber());
    }

    @Test // Test 2.2. Fallo: Lanzar una excepción cuando no se encuentra una solicitud de renta con el ID del arrendatario
    void testUpdateRentalRequestNotFound() {
        Long id = 1L;
        RequestARentalDTO requestDTO = new RequestARentalDTO();
        when(rentalRequestRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RentalRequestNotFoundException.class, () -> tenantRentalRequestService.updateRentalRequest(id, requestDTO));
    }

    

    @Test // Test 2.3. Fallo: Lanzar una excepción cuando cuando se proporciona una fecha de inicio inválida (anterior a la fecha actual).
    void testUpdateRentalRequestInvalidStartDate() {
        Long id = 1L;
        RequestARentalDTO requestDTO = new RequestARentalDTO();
        requestDTO.setStartDate(LocalDate.now().minusDays(1));  // Fecha de inicio inválida
        requestDTO.setEndDate(LocalDate.now().plusDays(1));

        TenantRentalRequest rentalRequest = new TenantRentalRequest();
        when(rentalRequestRepository.findById(id)).thenReturn(Optional.of(rentalRequest));

        assertThrows(InvalidStartDateException.class, () -> tenantRentalRequestService.updateRentalRequest(id, requestDTO));
    }


    @Test // Test 2.4. Fallo: Lanzar una excepción cuando cuando se proporciona una fecha de fin inválida (la misma que la fecha de inicio).
    void testUpdateRentalRequestInvalidEndDate() {
        Long id = 1L;
        RequestARentalDTO requestDTO = new RequestARentalDTO();
        requestDTO.setStartDate(LocalDate.now().plusDays(1));
        requestDTO.setEndDate(LocalDate.now().plusDays(1));  // Fecha de fin inválida

        TenantRentalRequest rentalRequest = new TenantRentalRequest();
        when(rentalRequestRepository.findById(id)).thenReturn(Optional.of(rentalRequest));

        assertThrows(InvalidEndDateException.class, () -> tenantRentalRequestService.updateRentalRequest(id, requestDTO));
    }
 
    // Test No. 5: reviewProperty

    @Test // Test 5.1. Exito: Calificar correctamente una propiedad
    void testReviewPropertySuccess() {
        Long requestId = 1L;
        TenantRentalRequest rentalRequest = new TenantRentalRequest();
        when(rentalRequestRepository.findById(requestId)).thenReturn(Optional.of(rentalRequest));

        tenantRentalRequestService.reviewProperty(requestId);

        assertTrue(rentalRequest.isPropertyReviewed());
        verify(rentalRequestRepository, times(1)).save(rentalRequest);
    }

    @Test // Test 5.2. Fallo: Lanzar una excepción cuando no se encuentra la solicitud de arrendamiento al intentar calificar una propiedad
    void testReviewPropertyNotFound() {
        Long requestId = 1L;
        when(rentalRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(RentalRequestNotFoundException.class, () -> tenantRentalRequestService.reviewProperty(requestId));
    }

    // Test No. 6: reviewLandlord

    @Test // Test 6.1. Exito: Calificar correctamente un arrendador
    void testReviewLandlordSuccess() {
        Long requestId = 1L;
        TenantRentalRequest rentalRequest = new TenantRentalRequest();
        when(rentalRequestRepository.findById(requestId)).thenReturn(Optional.of(rentalRequest));

        tenantRentalRequestService.reviewLandlord(requestId);

        assertTrue(rentalRequest.isLandlordReviewed());
        verify(rentalRequestRepository, times(1)).save(rentalRequest);
    }

    @Test // Test 6.2. Fallo: Lanzar una excepción cuando no se encuentra la solicitud de arrendamiento al intentar calificar un arrendador
    void testReviewLandlordNotFound() {
        Long requestId = 1L;
        when(rentalRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(RentalRequestNotFoundException.class, () -> tenantRentalRequestService.reviewLandlord(requestId));
    }
}
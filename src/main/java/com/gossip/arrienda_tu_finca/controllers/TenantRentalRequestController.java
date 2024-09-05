package com.gossip.arrienda_tu_finca.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gossip.arrienda_tu_finca.entities.TenantRentalRequest;
import com.gossip.arrienda_tu_finca.services.TenantRentalRequestService;
import com.gossip.arrienda_tu_finca.dto.RequestARentalDTO;
import com.gossip.arrienda_tu_finca.dto.TenantRentalRequestDTO;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/tenant")
@AllArgsConstructor
public class TenantRentalRequestController {

    private final TenantRentalRequestService tenantRentalRequestService;

    // Ver solicitudes de arriendo

    @GetMapping
    public ResponseEntity<List<TenantRentalRequestDTO>> getAllRentalsRequests(@RequestParam String email) {
        List<TenantRentalRequestDTO> rentalRequests = tenantRentalRequestService.getAllRequestsByTenant(email);
        return ResponseEntity.ok(rentalRequests);
    }

    // Solicitar arriendo

    @PutMapping("/{id}")
    public ResponseEntity<TenantRentalRequest> updateRentalRequest(@PathVariable Long id, @RequestBody RequestARentalDTO requestDTO) {
        
        TenantRentalRequest updatedRequest = tenantRentalRequestService.updateRentalRequest(id, requestDTO);
        return ResponseEntity.ok(updatedRequest);
    }

    // Calificar finca

    @PutMapping("/{requestId}/propertyReview")
    public ResponseEntity<String> reviewProperty(@PathVariable Long requestId) {
        tenantRentalRequestService.reviewProperty(requestId);
        return new ResponseEntity<>("Propiedad calificada", HttpStatus.OK);
    }

    // Calificar arrendador

    @PutMapping("/{requestId}/landlordReview")
    public ResponseEntity<String> reviewLandlord(@PathVariable Long requestId) {
        tenantRentalRequestService.reviewLandlord(requestId);
        return new ResponseEntity<>("Arrendador calificado", HttpStatus.OK);
    }  
}

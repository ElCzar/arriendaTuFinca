package com.gossip.arrienda_tu_finca.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gossip.arrienda_tu_finca.entities.RentalRequest;
import com.gossip.arrienda_tu_finca.services.RentalRequestService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/rental-requests")
@AllArgsConstructor
public class RentalRequestController {

    private final RentalRequestService rentalRequestService;

    @GetMapping("/owner/{email}")
    public ResponseEntity<List<RentalRequest>> getRequestsByOwner(@PathVariable String email) {
        List<RentalRequest> requests = rentalRequestService.getRequestsByOwner(email);
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<RentalRequest>> getRequestsByProperty(@PathVariable Long propertyId) {
        List<RentalRequest> requests = rentalRequestService.getRequestsByProperty(propertyId);
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @PutMapping("/{requestId}/accept")
    public ResponseEntity<String> acceptRequest(@PathVariable Long requestId) {
        rentalRequestService.acceptRequest(requestId);
        return new ResponseEntity<>("Solicitud de arriendo aceptada", HttpStatus.OK);
    }

    @PutMapping("/{requestId}/cancel")
    public ResponseEntity<String> cancelRequest(@PathVariable Long requestId) {
        rentalRequestService.cancelRequest(requestId);
        return new ResponseEntity<>("Solicitud de arriendo cancelada", HttpStatus.OK);
    }

    @PutMapping("/{requestId}/review")
    public ResponseEntity<String> reviewRenter(@PathVariable Long requestId) {
        rentalRequestService.reviewRenter(requestId);
        return new ResponseEntity<>("Arrendatario calificado", HttpStatus.OK);
    }

    @PutMapping("/{requestId}/complete")
    public ResponseEntity<String> completeRequest(@PathVariable Long requestId) {
        rentalRequestService.completeRequest(requestId);
        return new ResponseEntity<>("Solicitud de arriendo completada", HttpStatus.OK);
    }

    @PutMapping("/{requestId}/reject")
    public ResponseEntity<String> rejectRequest(@PathVariable Long requestId) {
        rentalRequestService.rejectRequest(requestId);
        return new ResponseEntity<>("Solicitud de arriendo rechazada", HttpStatus.OK);
    }

    // New method
    @PutMapping("/{requestId}/approve")
    public ResponseEntity<String> approveRequest(@PathVariable Long requestId) {
        rentalRequestService.approveRequest(requestId);
        return new ResponseEntity<>("Solicitud de arriendo aprobada", HttpStatus.OK);
    }
}
package com.gossip.arrienda_tu_finca.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gossip.arrienda_tu_finca.dto.RentalRequestDto;
import com.gossip.arrienda_tu_finca.services.RentalRequestService;
import com.gossip.arrienda_tu_finca.exceptions.RentalRequestNotFoundException;

@RestController
@RequestMapping("/rental-requests")
public class RentalRequestController {
    private final RentalRequestService rentalRequestService;
    private final ModelMapper modelMapper;

    @Autowired
    public RentalRequestController(RentalRequestService rentalRequestService, ModelMapper modelMapper) {
        this.rentalRequestService = rentalRequestService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/owner/{email}")
    public ResponseEntity<List<RentalRequestDto>> getRequestsByOwner(@PathVariable String email) {
        List<RentalRequestDto> requests = rentalRequestService.getRequestsByOwner(email)
            .stream()
            .map( request -> modelMapper.map(request, RentalRequestDto.class))
            .collect(Collectors.toList());
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<RentalRequestDto>> getRequestsByProperty(@PathVariable Long propertyId) {
        List<RentalRequestDto> requests = rentalRequestService.getRequestsByProperty(propertyId)
            .stream()
            .map( request -> modelMapper.map(request, RentalRequestDto.class))
            .collect(Collectors.toList());
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

    @PutMapping("/{requestId}/approve")
    public ResponseEntity<String> approveRequest(@PathVariable Long requestId) {
        try {
            rentalRequestService.approveRequest(requestId);
            return new ResponseEntity<>("Solicitud de arriendo aprobada", HttpStatus.OK);
        } catch (RentalRequestNotFoundException e) {
            return new ResponseEntity<>("Solicitud de arriendo no encontrada", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{requestId}/pay")
    public ResponseEntity<String> payRequest(@PathVariable Long requestId) {
        try {
            rentalRequestService.payRequest(requestId);
            return new ResponseEntity<>("Solicitud de arriendo pagada", HttpStatus.OK);
        } catch (RentalRequestNotFoundException e) {
            return new ResponseEntity<>("Solicitud de arriendo no encontrada", HttpStatus.NOT_FOUND);
        }
    }
}
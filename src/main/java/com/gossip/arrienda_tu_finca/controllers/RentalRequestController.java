package com.gossip.arrienda_tu_finca.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * Creates a rental request from a renter to a property
     * @param propertyId
     * @param rentalRequest
     * @return
     */
    @PostMapping("/create/{propertyId}")
    public ResponseEntity<String> createRequest(@PathVariable Long propertyId, @RequestBody RentalRequestDto rentalRequest) {
        rentalRequestService.createRequest(propertyId, rentalRequest);
        return new ResponseEntity<>("Solicitud de arriendo creada", HttpStatus.OK);
    }

    /**
     * Gets all rental requests given the host email
     * @param email
     * @return
     */
    @GetMapping("/host")
    public ResponseEntity<List<RentalRequestDto>> getRequestsByHost(@RequestBody String email) {
        List<RentalRequestDto> requests = rentalRequestService.getRequestsByHost(email)
            .stream()
            .map( request -> modelMapper.map(request, RentalRequestDto.class))
            .collect(Collectors.toList());
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    /**
     * Gets all rental requests given the renter email
     * @param email
     * @return
     */
    @GetMapping("/requester")
    public ResponseEntity<List<RentalRequestDto>> getRequestsByRenter(@RequestBody String email) {
        List<RentalRequestDto> requests = rentalRequestService.getRequestsByRenter(email)
            .stream()
            .map( request -> modelMapper.map(request, RentalRequestDto.class))
            .collect(Collectors.toList());
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    /**
     * Gets all rental requests given the property ID
     * @param propertyId
     * @return
     */
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<RentalRequestDto>> getRequestsByProperty(@PathVariable Long propertyId) {
        List<RentalRequestDto> requests = rentalRequestService.getRequestsByProperty(propertyId)
            .stream()
            .map( request -> modelMapper.map(request, RentalRequestDto.class))
            .collect(Collectors.toList());
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    /**
     * A renter cancels a rental request
     * @param requestId
     * @return
     */
    @PutMapping("/cancel/{requestId}")
    public ResponseEntity<String> cancelRequest(@PathVariable Long requestId) {
        rentalRequestService.cancelRequest(requestId);
        return new ResponseEntity<>("Solicitud de arriendo cancelada", HttpStatus.OK);
    }

    /**
     * A host completes a rental request
     * @param requestId
     * @return
     */
    @PutMapping("/complete/{requestId}")
    public ResponseEntity<String> completeRequest(@PathVariable Long requestId) {
        rentalRequestService.completeRequest(requestId);
        return new ResponseEntity<>("Solicitud de arriendo completada", HttpStatus.OK);
    }

    /**
     * A host rejects a rental request
     * @param requestId
     * @return
     */
    @PutMapping("/reject/{requestId}")
    public ResponseEntity<String> rejectRequest(@PathVariable Long requestId) {
        rentalRequestService.rejectRequest(requestId);
        return new ResponseEntity<>("Solicitud de arriendo rechazada", HttpStatus.OK);
    }

    /**
     * A host approves a rental request
     * @param requestId
     * @return
     */
    @PutMapping("/approve/{requestId")
    public ResponseEntity<String> approveRequest(@PathVariable Long requestId) {
        try {
            rentalRequestService.approveRequest(requestId);
            return new ResponseEntity<>("Solicitud de arriendo aprobada", HttpStatus.OK);
        } catch (RentalRequestNotFoundException e) {
            return new ResponseEntity<>("Solicitud de arriendo no encontrada", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * A renter pays a rental request 
     * @param requestId
     * @return
     */
    @PutMapping("/pay/{requestId}")
    public ResponseEntity<String> payRequest(@PathVariable Long requestId) {
        try {
            rentalRequestService.payRequest(requestId);
            return new ResponseEntity<>("Solicitud de arriendo pagada", HttpStatus.OK);
        } catch (RentalRequestNotFoundException e) {
            return new ResponseEntity<>("Solicitud de arriendo no encontrada", HttpStatus.NOT_FOUND);
        }
    }

}
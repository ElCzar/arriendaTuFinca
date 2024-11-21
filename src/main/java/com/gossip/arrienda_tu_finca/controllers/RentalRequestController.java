package com.gossip.arrienda_tu_finca.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gossip.arrienda_tu_finca.dto.CommentDTO;
import com.gossip.arrienda_tu_finca.dto.RentalRequestCreateDTO;
import com.gossip.arrienda_tu_finca.dto.RentalRequestDto;
import com.gossip.arrienda_tu_finca.exceptions.RentalRequestNotFoundException;
import com.gossip.arrienda_tu_finca.services.RentalRequestService;

@RestController
@RequestMapping("/rental-requests")
public class RentalRequestController {
    private final RentalRequestService rentalRequestService;

    @Autowired
    public RentalRequestController(RentalRequestService rentalRequestService) {
        this.rentalRequestService = rentalRequestService;
    }

    /**
     * Creates a rental request from a renter to a property
     * @param propertyId
     * @param rentalRequest
     * @return
     */
    @PostMapping("/create/{propertyId}")
    public ResponseEntity<String> createRequest(@PathVariable Long propertyId, @RequestBody RentalRequestCreateDTO rentalRequest) {
        rentalRequestService.createRequest(propertyId, rentalRequest);
        return new ResponseEntity<>("Solicitud de arriendo creada", HttpStatus.OK);
    }

    /**
     * Gets all rental requests given the host email
     * @param email
     * @return
     */
    @GetMapping("/host")
    public ResponseEntity<List<RentalRequestDto>> getRequestsByHost(@RequestParam String email) {
        List<RentalRequestDto> requests = rentalRequestService.getRequestsByHost(email);
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    /**
     * Gets all rental requests given the renter email
     * @param email
     * @return
     */
    @GetMapping("/renter")
    public ResponseEntity<List<RentalRequestDto>> getRequestsByRenter(@RequestParam String email) {
        List<RentalRequestDto> requests = rentalRequestService.getRequestsByRenter(email);
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    /**
     * Gets all rental requests given the property ID
     * @param propertyId
     * @return
     */
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<RentalRequestDto>> getRequestsByProperty(@PathVariable Long propertyId) {
        List<RentalRequestDto> requests = rentalRequestService.getRequestsByProperty(propertyId);
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
    @PutMapping("/approve/{requestId}")
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

    /**
     * A renter reviews a property given the rental request ID
     * @param requestId
     * @param commentDto
     * @return ResponseEntity<Void> which indicates the success of the operation
     */
    @PostMapping("/review-property/{requestId}")
    public ResponseEntity<Void> reviewProperty(@PathVariable Long requestId, @RequestBody CommentDTO commentDto) {
        rentalRequestService.reviewProperty(requestId, commentDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * A renter reviews a host given the rental request ID
     * @param requestId
     * @param commentDto
     * @return ResponseEntity<Void> which indicates the success of the operation
     */
    @PostMapping("/review-host/{requestId}")
    public ResponseEntity<Void> reviewHost(@PathVariable Long requestId, @RequestBody CommentDTO commentDto) {
        rentalRequestService.reviewHost(requestId, commentDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * A host reviews a renter given the rental request ID
     * @param requestId
     * @param commentDto
     * @return ResponseEntity<Void> which indicates the success of the operation
     */
    @PostMapping("/review-renter/{requestId}")
    public ResponseEntity<Void> reviewRenter(@PathVariable Long requestId, @RequestBody CommentDTO commentDto) {
        rentalRequestService.reviewRenter(requestId, commentDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     * Get the comments that rate the renter given the renter email
     * @param requestId
     * @return ResponseEntity<List<CommentDTO>> which contains the comments
     */
    @GetMapping("/renter-comments")
    public ResponseEntity<List<CommentDTO>> getRenterComments(@RequestParam String email) {
        List<CommentDTO> comments = rentalRequestService.getRenterComments(email);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    /**
     * Get the comments that rate the host given the host email
     * @param email
     * @return ResponseEntity<List<CommentDTO>> which contains the comments 
     */
    @GetMapping("/host-comments")
    public ResponseEntity<List<CommentDTO>> getHostComments(@RequestParam String email) {
        List<CommentDTO> comments = rentalRequestService.getHostComments(email);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    /**
     * Get the comments that rate the property given the property ID
     * @param requestId
     * @return ResponseEntity<List<CommentDTO>> which contains the comments
     */
    @GetMapping("/property-comments/{propertyId}")
    public ResponseEntity<List<CommentDTO>> getPropertyComments(@PathVariable Long propertyId) {
        List<CommentDTO> comments = rentalRequestService.getPropertyComments(propertyId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
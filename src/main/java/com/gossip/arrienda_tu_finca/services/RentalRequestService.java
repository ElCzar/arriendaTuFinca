package com.gossip.arrienda_tu_finca.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.gossip.arrienda_tu_finca.repositories.RentalRequestRepository;
import com.gossip.arrienda_tu_finca.entities.RentalRequest;
import com.gossip.arrienda_tu_finca.exceptions.RentalRequestNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RentalRequestService {
    
    private final RentalRequestRepository rentalRequestRepository;
    private static final String RENTAL_REQUEST_NOT_FOUND = "Solicitud de arriendo no encontrada";

    
    public List<RentalRequest> getRequestsByProperty(Long propertyId) {
        return rentalRequestRepository.findByPropertyId(propertyId);
    }

    public List<RentalRequest> getRequestsByOwner(String ownerEmail) {
        List<RentalRequest> requests = rentalRequestRepository.findByPropertyOwnerEmail(ownerEmail);
        if (requests.isEmpty()) {
            throw new RentalRequestNotFoundException("No se encontraron solicitudes de arriendo para el propietario con email: " + ownerEmail);
        }
        return requests;
    }

    
    public void acceptRequest(Long requestId) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RentalRequest request = optionalRequest.get();
            
            request.setAccepted(true);
            rentalRequestRepository.save(request);
        } else {
            throw new RentalRequestNotFoundException(RENTAL_REQUEST_NOT_FOUND);
        }
    }

    
    public void cancelRequest(Long requestId) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RentalRequest request = optionalRequest.get();
            
            request.setCanceled(true);
            rentalRequestRepository.save(request);
        } else {
            throw new RentalRequestNotFoundException(RENTAL_REQUEST_NOT_FOUND);
        }
    }

    
    public void reviewRenter(Long requestId) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RentalRequest request = optionalRequest.get();
          
            request.setReviewed(true);
            rentalRequestRepository.save(request);
        } else {
            throw new RentalRequestNotFoundException(RENTAL_REQUEST_NOT_FOUND);
        }
    }

    public void completeRequest(Long requestId) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RentalRequest request = optionalRequest.get();
            request.setCompleted(true);
            rentalRequestRepository.save(request);
        } else {
            throw new RentalRequestNotFoundException(RENTAL_REQUEST_NOT_FOUND);
        }
    }

    public void rejectRequest(Long requestId) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RentalRequest request = optionalRequest.get();
            request.setRejected(true);
            rentalRequestRepository.save(request);
        } else {
            throw new RentalRequestNotFoundException(RENTAL_REQUEST_NOT_FOUND);
        }
    }

    public RentalRequest approveRequest(Long requestId) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RentalRequest request = optionalRequest.get();
            request.setApproved(true);
            rentalRequestRepository.save(request);
            return request;
        } else {
            throw new RentalRequestNotFoundException(RENTAL_REQUEST_NOT_FOUND);
        }
    }

    public RentalRequest payRequest(Long requestId) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RentalRequest request = optionalRequest.get();
            request.setPaid(true);
            rentalRequestRepository.save(request);
            return request;
        } else {
            throw new RentalRequestNotFoundException(RENTAL_REQUEST_NOT_FOUND);
        }
    }

    
}
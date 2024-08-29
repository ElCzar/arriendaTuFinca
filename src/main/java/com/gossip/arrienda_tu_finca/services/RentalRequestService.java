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
            throw new RentalRequestNotFoundException("Solicitud de arriendo no encontrada");
        }
    }

    
    public void cancelRequest(Long requestId) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RentalRequest request = optionalRequest.get();
            
            request.setCanceled(true);
            rentalRequestRepository.save(request);
        } else {
            throw new RentalRequestNotFoundException("Solicitud de arriendo no encontrada");
        }
    }

    
    public void reviewRenter(Long requestId) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RentalRequest request = optionalRequest.get();
          
            request.setReviewed(true);
            rentalRequestRepository.save(request);
        } else {
            throw new RentalRequestNotFoundException("Solicitud de arriendo no encontrada");
        }
    }

    public void completeRequest(Long requestId) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RentalRequest request = optionalRequest.get();
            request.setCompleted(true);
            rentalRequestRepository.save(request);
        } else {
            throw new RentalRequestNotFoundException("Solicitud de arriendo no encontrada");
        }
    }

    public void rejectRequest(Long requestId) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RentalRequest request = optionalRequest.get();
            request.setRejected(true);
            rentalRequestRepository.save(request);
        } else {
            throw new RentalRequestNotFoundException("Solicitud de arriendo no encontrada");
        }
    }

    public void approveRequest(Long requestId) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RentalRequest request = optionalRequest.get();
            request.setApproved(true);
            rentalRequestRepository.save(request);
        } else {
            throw new RentalRequestNotFoundException("Solicitud de arriendo no encontrada");
        }
    }
}
package com.gossip.arrienda_tu_finca.services;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalDate;

import com.gossip.arrienda_tu_finca.repositories.PropertyRepository;
import com.gossip.arrienda_tu_finca.repositories.RentalRequestRepository;
import com.gossip.arrienda_tu_finca.repositories.UserRepository;
import com.gossip.arrienda_tu_finca.dto.RentalRequestDto;
import com.gossip.arrienda_tu_finca.entities.Property;
import com.gossip.arrienda_tu_finca.entities.RentalRequest;
import com.gossip.arrienda_tu_finca.exceptions.InvalidAmountOfResidentsException;
import com.gossip.arrienda_tu_finca.exceptions.InvalidDateException;
import com.gossip.arrienda_tu_finca.exceptions.InvalidPaymentException;
import com.gossip.arrienda_tu_finca.exceptions.InvalidReviewException;
import com.gossip.arrienda_tu_finca.exceptions.PropertyNotFoundException;
import com.gossip.arrienda_tu_finca.exceptions.RentalRequestNotFoundException;

@Service
public class RentalRequestService {

    private final RentalRequestRepository rentalRequestRepository;
    private final ModelMapper modelMapper;
    private UserRepository userRepository;
    private PropertyRepository propertyRepository;
    private static final String RENTAL_REQUEST_NOT_FOUND = "Solicitud de arriendo no encontrada";

    @Autowired
    public RentalRequestService(RentalRequestRepository rentalRequestRepository, ModelMapper modelMapper, UserRepository userRepository, PropertyRepository propertyRepository) {
        this.rentalRequestRepository = rentalRequestRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
    }

    /**
     * Creates a rental request from a renter to a property
     * @param propertyId
     * @param rentalRequest
     * @throws PropertyNotFoundException
     * @throws InvalidRenterException 
     */
    public void createRequest(Long propertyId, RentalRequestDto rentalRequest) {
        RentalRequest request = modelMapper.map(rentalRequest, RentalRequest.class);

        Long userId = userRepository.findIdByEmail(rentalRequest.getRequesterEmail());
        if (userId == null) {
            throw new PropertyNotFoundException("Usuario con email " + rentalRequest.getRequesterEmail() + " mo fue encontrado");
        }
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Propiedad con ID " + propertyId + " no fue encontrada"));
        LocalDate today = LocalDate.now();
        if (rentalRequest.getArrivalDate().isBefore(today)) {
            throw new InvalidDateException("La fecha inicial no puede ser anterior a la fecha actual");
        }
        if (rentalRequest.getDepartureDate().isBefore(rentalRequest.getArrivalDate().plusDays(1))) {
            throw new InvalidDateException("La fecha final no puede ser anterior a un día posterior a la fecha inicial");
        }
        if (rentalRequest.getAmountOfResidents() > property.getAmountOfResidents()) {
            throw new InvalidAmountOfResidentsException("La cantidad de residentes no puede ser superior a la permitida en la propiedad");
        }
        
        request.setProperty(property);
        request.setRequester(userRepository.findById(userId).get());
        request.setRequestDateTime(LocalDateTime.now()); 
        request.setRejected(false);
        request.setCanceled(false); 
        request.setPaid(false);
        request.setCompleted(false); 
        request.setApproved(false);
        request.setExpired(false);
        rentalRequestRepository.save(request);
    }

    /**
     * Gets all rental requests given the property ID
     * @param propertyId ID of the property
     * @throws RentalRequestNotFoundException if no request were found
     * @return List<RentalRequest> of the request about the property
     */
    public List<RentalRequest> getRequestsByProperty(Long propertyId) {
        List<RentalRequest> requests = rentalRequestRepository.findByPropertyId(propertyId);
        if (requests.isEmpty()) {
            throw new RentalRequestNotFoundException("No se encontraron solicitudes de arriendo para la propiedad con ID: " + propertyId);
        }
        return requests;
    }

    /**
     * Gets all rental request given the host email
     * @param hostEmail email of the host
     * @return
     */
    public List<RentalRequest> getRequestsByHost(String hostEmail) {
        List<RentalRequest> requests = rentalRequestRepository.findByPropertyOwnerEmail(hostEmail);
        if (requests.isEmpty()) {
            throw new RentalRequestNotFoundException("No se encontraron solicitudes de arriendo para el propietario con email: " + hostEmail);
        }
        return requests;
    }

    /**
     * Gets all the rental request given the renter email
     * @param renterEmail email of the renter
     * @return
     */
    public List<RentalRequest> getRequestsByRenter(String renterEmail) {
        List<RentalRequest> requests = rentalRequestRepository.findByRequesterEmail(renterEmail);
        if (requests.isEmpty()) {
            throw new RentalRequestNotFoundException("No se encontraron solicitudes de arriendo para el arrendatario con email: " + renterEmail);
        }
        return requests;
    }

    /**
     * Renter cancels the rental request given the request ID
     * @param requestId
     */
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

    /**
     * Host completes the rental request given the request ID
     * @param requestId
     */
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

    /**
     * Host rejects the rental request given the request ID
     * @param requestId
     */
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

    /**
     * Host approve the rental request given the request ID
     * @param requestId
     * @return
     */
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

    /**
     * Renter pays the rental request given the request ID
     * @param requestId
     * @return
     */
    public RentalRequest payRequest(Long requestId) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RentalRequest request = optionalRequest.get();
            if (!request.isApproved()) {
                throw new InvalidPaymentException("La solicitud de arriendo no ha sido aceptada.");
             }
             if (request.isPaid()) {
                throw new InvalidPaymentException("La solicitud de arriendo ya ha sido pagada.");
             }
             if (request.isExpired()) {
                 throw new InvalidPaymentException("La solicitud de arriendo ha expirado.");
             }
            request.setPaid(true);
            rentalRequestRepository.save(request);
            return request;
        } else {
            throw new RentalRequestNotFoundException(RENTAL_REQUEST_NOT_FOUND);
        }
    }

    // Reviews of the different entities involved
}
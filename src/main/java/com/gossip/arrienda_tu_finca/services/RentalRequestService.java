package com.gossip.arrienda_tu_finca.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalDate;

import com.gossip.arrienda_tu_finca.repositories.PropertyRepository;
import com.gossip.arrienda_tu_finca.repositories.RentalRequestRepository;
import com.gossip.arrienda_tu_finca.repositories.UserRepository;
import com.gossip.arrienda_tu_finca.dto.PaymentDTO;
import com.gossip.arrienda_tu_finca.dto.PropertyCreateDTO;
import com.gossip.arrienda_tu_finca.dto.PropertyDTO;
import com.gossip.arrienda_tu_finca.dto.RentalRequestCreateDTO;
import com.gossip.arrienda_tu_finca.dto.RentalRequestDto;
import com.gossip.arrienda_tu_finca.dto.RentalRequestViewDTO;
import com.gossip.arrienda_tu_finca.entities.Property;
import com.gossip.arrienda_tu_finca.entities.RentalRequest;
import com.gossip.arrienda_tu_finca.exceptions.InvalidAmountOfResidentsException;
import com.gossip.arrienda_tu_finca.exceptions.InvalidDateException;
import com.gossip.arrienda_tu_finca.exceptions.InvalidPaymentException;
import com.gossip.arrienda_tu_finca.exceptions.PropertyNotFoundException;
import com.gossip.arrienda_tu_finca.exceptions.RentalRequestNotFoundException;
import com.gossip.arrienda_tu_finca.exceptions.UserNotFoundException;

@Service
public class RentalRequestService {

    private final RentalRequestRepository rentalRequestRepository;
    private final ModelMapper modelMapper;
    private UserRepository userRepository;
    private PropertyRepository propertyRepository;
    private static final String RENTAL_REQUEST_NOT_FOUND = "Solicitud de arriendo no encontrada";

    @Autowired
    public RentalRequestService(RentalRequestRepository rentalRequestRepository, ModelMapper modelMapper) {
        this.rentalRequestRepository = rentalRequestRepository;
        this.modelMapper = modelMapper;
    }

    public void createRequest(RentalRequestDto rentalRequest) {
        RentalRequest request = modelMapper.map(rentalRequest, RentalRequest.class);
        rentalRequestRepository.save(request);
    }
    
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

    // Arrendatario

    // Calificar arrendador
    public void reviewLessor(Long requestId) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RentalRequest request = optionalRequest.get();
          
            request.setReviewedLessor(true);
            rentalRequestRepository.save(request);
        } else {
            throw new RentalRequestNotFoundException(RENTAL_REQUEST_NOT_FOUND);
        }
    }

    // Calificar arrendatario
    public void reviewProperty(Long requestId) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RentalRequest request = optionalRequest.get();
          
            request.setReviewedProperty(true);
            rentalRequestRepository.save(request);
        } else {
            throw new RentalRequestNotFoundException(RENTAL_REQUEST_NOT_FOUND);
        }
    }

    // Crear solicitud de arriendo
    public RentalRequestDto createRentalRequest(Long propertyId, RentalRequestCreateDTO rentalRequestCreateDTO) {

        RentalRequest rentalRequest = modelMapper.map(rentalRequestCreateDTO,RentalRequest.class);
  
        Long userId = userRepository.findIdByEmail(rentalRequestCreateDTO.getRequesterEmail());

        if (userId == null) {
            throw new PropertyNotFoundException("Usuario con email " + rentalRequestCreateDTO.getRequesterEmail() + " mo fue encontrado");
        }

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Propiedad con ID " + propertyId + " no fue encontrada"));

        LocalDate today = LocalDate.now();
        if (rentalRequestCreateDTO.getStartDate().isBefore(today)) {
            throw new InvalidDateException("La fecha inicial no puede ser anterior a la fecha actual");
        }
        if (rentalRequestCreateDTO.getEndDate().isBefore(rentalRequestCreateDTO.getStartDate().plusDays(1))) {
            throw new InvalidDateException("La fecha final no puede ser anterior a un día posterior a la fecha inicial");
        }

        if (rentalRequestCreateDTO.getAmountOfResidents() > property.getAmountOfResidents()) {
            throw new InvalidAmountOfResidentsException("La cantidad de residentes no puede ser superior a la permitida en la propiedad");
        }

        rentalRequest.setProperty(property);
        rentalRequest.setRequester(userRepository.findById(userId).get());
        rentalRequest.setRequestDateTime(LocalDateTime.now()); 
        rentalRequest.setAccepted(false); 
        rentalRequest.setRejected(false);
        rentalRequest.setCanceled(false); 
        rentalRequest.setPaid(false);
        rentalRequest.setReviewed(false); 
        rentalRequest.setReviewedLessor(false);
        rentalRequest.setReviewedProperty(false);
        rentalRequest.setCompleted(false); 
        rentalRequest.setApproved(false);
        rentalRequest.setExpired(false);

        RentalRequest savedRentalRequest = rentalRequestRepository.save(rentalRequest);
        return modelMapper.map(savedRentalRequest, RentalRequestDto.class);
    }

    // Obtener las solicitudes de arriendo de un requester (email)
    public List<RentalRequestViewDTO> getRequestsByRequesterEmail(String requesterEmail) {
        List<RentalRequest> rentalRequests = rentalRequestRepository.findByRequesterEmailOrderByRequestDateTime(requesterEmail);

        if (rentalRequests.isEmpty()) {
            throw new RentalRequestNotFoundException("No se encontraron solicitudes de arriendo para el arrendatario con email: "  + requesterEmail);
        }

        return rentalRequests.stream()
            .map(request -> modelMapper.map(request, RentalRequestViewDTO.class)) // Mapea la entidad a DTO
            .collect(Collectors.toList());
    }

    // Realizar el pago de una solicitud de arriendo
    public RentalRequestDto payRequest(Long requestId, PaymentDTO paymentDTO) {
        RentalRequest rentalRequest = rentalRequestRepository.findById(requestId)
        .orElseThrow(() -> new RentalRequestNotFoundException("Solicitud de arriendo con ID" + requestId + " no fue encontrada"));
         
        if (!rentalRequest.isAccepted()) {
           throw new InvalidPaymentException("La solicitud de arriendo no ha sido aceptada.");
        }
        if (rentalRequest.isPaid()) {
           throw new InvalidPaymentException("La solicitud de arriendo ya ha sido pagada.");
        }
        if (rentalRequest.isExpired()) {
            throw new InvalidPaymentException("La solicitud de arriendo ha expirado.");
        }

        rentalRequest.setPaid(true);
        modelMapper.map(paymentDTO, rentalRequest); 
        RentalRequest updatedRentalRequest = rentalRequestRepository.save(rentalRequest);
        return modelMapper.map(updatedRentalRequest, RentalRequestDto.class);
    }
}
package com.gossip.arrienda_tu_finca.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.gossip.arrienda_tu_finca.repositories.PropertyRepository;
import com.gossip.arrienda_tu_finca.repositories.TenantRentalRequestRepository;
import com.gossip.arrienda_tu_finca.dto.PaymentDTO;
import com.gossip.arrienda_tu_finca.dto.PropertyDTO;
import com.gossip.arrienda_tu_finca.dto.RequestARentalDTO;
import com.gossip.arrienda_tu_finca.entities.Property;
import com.gossip.arrienda_tu_finca.entities.TenantRentalRequest;
import com.gossip.arrienda_tu_finca.exceptions.InvalidEndDateException;
import com.gossip.arrienda_tu_finca.exceptions.InvalidPeopleNumberException;
import com.gossip.arrienda_tu_finca.exceptions.InvalidStartDateException;
import com.gossip.arrienda_tu_finca.exceptions.RentalRequestNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TenantRentalRequestService {
    
    private final TenantRentalRequestRepository rentalRequestRepository;

    public TenantRentalRequest updateRentalRequest(Long id, RequestARentalDTO requestDTO) {
        TenantRentalRequest rentalRequest = rentalRequestRepository.findById(id)
                .orElseThrow(() -> new RentalRequestNotFoundException("Rentall Request not found"));

        validateDates(requestDTO.getStartDate(), requestDTO.getEndDate());
        validatePeopleNumber(requestDTO.getPeopleNumber(), rentalRequest.getProperty().getPeopleNumber());

        rentalRequest.setArrivalDate(requestDTO.getStartDate());
        rentalRequest.setDepartureDate(requestDTO.getEndDate());
        rentalRequest.setPeopleNumber(requestDTO.getPeopleNumber());

        return rentalRequestRepository.save(rentalRequest);
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();

        if (startDate.isBefore(today)) {
            throw new InvalidStartDateException("Invalid start date");
        }

        if (endDate.isBefore(startDate.plusDays(1))) {
            throw new InvalidEndDateException("Invalid end date");
        }
    }

    private void validatePeopleNumber(Integer requestedPeople, Integer maxPeople) {
        if (requestedPeople > maxPeople) {
            throw new InvalidPeopleNumberException("Invalid people number");
        }
    }

    // Calificar arrendador
    public void reviewLandlord(Long requestId) {
        Optional<TenantRentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            TenantRentalRequest request = optionalRequest.get();
          
            request.setLandlordReviewed(true);
            rentalRequestRepository.save(request);
        } else {
            throw new RentalRequestNotFoundException("Rental request not found");
        }
    }

    // Calificar finca
    public void reviewProperty(Long requestId) { 
        Optional<TenantRentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            TenantRentalRequest request = optionalRequest.get();
          
            request.setPropertyReviewed(true);
            rentalRequestRepository.save(request);
        } else {
            throw new RentalRequestNotFoundException("Rental request not found");
        }
    }

    // Convierte una entidad Property en PropertyDTO
    private PropertyDTO mapToDTO(Property property) {
        PropertyDTO dto = new PropertyDTO();
        dto.setId(property.getId());
        dto.setName(property.getName());
        dto.setDescription(property.getDescription());
        dto.setMunicipality(property.getMunicipality());
        dto.setTypeOfEntrance(property.getTypeOfEntrance());
        dto.setPeopleNumber(property.getPeopleNumber());
        dto.setAddress(property.getAddress());
        dto.setPricePerNight(property.getPricePerNight());
        dto.setAmountOfRooms(property.getAmountOfRooms());
        dto.setAmountOfBathrooms(property.getAmountOfBathrooms());
        dto.setPetFriendly(property.isPetFriendly());
        dto.setHasPool(property.isHasPool());
        dto.setHasGril(property.isHasGril());
        dto.setAvailable(property.isAvailable());
        return dto;
    }
}

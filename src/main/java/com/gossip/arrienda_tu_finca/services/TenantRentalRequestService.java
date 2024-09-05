package com.gossip.arrienda_tu_finca.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import com.gossip.arrienda_tu_finca.repositories.PropertyRepository;
import com.gossip.arrienda_tu_finca.repositories.TenantRentalRequestRepository;
import com.gossip.arrienda_tu_finca.dto.PaymentDTO;
import com.gossip.arrienda_tu_finca.dto.PropertyListDTO;
import com.gossip.arrienda_tu_finca.dto.RequestARentalDTO;
import com.gossip.arrienda_tu_finca.dto.TenantRentalRequestDTO;
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

    @Autowired
    private final TenantRentalRequestRepository rentalRequestRepository;

    @Autowired
    private ModelMapper modelMapper;  

    //Llenar formulario de arrendamiento
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

    private void validateDates(LocalDate startDate, LocalDate endDate) { // Validación de las fechas de inicio y final de la renta
        LocalDate today = LocalDate.now();

        if (startDate.isBefore(today)) {
            throw new InvalidStartDateException("Invalid start date");
        }

        if (endDate.isBefore(startDate.plusDays(1))) {
            throw new InvalidEndDateException("Invalid end date");
        }
    }

    private void validatePeopleNumber(Integer requestedPeople, Integer maxPeople) { // Validación del numero de personas de la renta
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

    // Mostrar solicitudes de renta del arrendatario
    public List<TenantRentalRequestDTO> getAllRequestsByTenant(String email) {
        List<TenantRentalRequest> tRentalRequests = rentalRequestRepository.findAllRentalRequestsByEmail(email);
        if (tRentalRequests.isEmpty()) {
            throw new RentalRequestNotFoundException("Rental request not found");
        }
        return tRentalRequests.stream()
                .map(tenantRentalRequest -> modelMapper.map(tenantRentalRequest, TenantRentalRequestDTO.class))
                .collect(Collectors.toList());          
    }


}

package com.gossip.arrienda_tu_finca.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gossip.arrienda_tu_finca.entities.RentalRequest;

@Repository
public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long> {

    
    List<RentalRequest> findByPropertyOwnerEmail(String ownerEmail);

    
    List<RentalRequest> findByPropertyId(Long propertyId);

    

}
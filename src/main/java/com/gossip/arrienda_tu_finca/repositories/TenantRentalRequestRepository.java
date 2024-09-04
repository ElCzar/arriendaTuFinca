package com.gossip.arrienda_tu_finca.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gossip.arrienda_tu_finca.entities.TenantRentalRequest;

@Repository
public interface TenantRentalRequestRepository extends JpaRepository<TenantRentalRequest, Long> {

    
    List<TenantRentalRequest> findByPropertyTenantEmail(String tenantEmail);

    
    List<TenantRentalRequest> findByPropertyId(Long propertyId);

}
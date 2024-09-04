package com.gossip.arrienda_tu_finca.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gossip.arrienda_tu_finca.entities.TenantRentalRequest;

@Repository
public interface TenantRentalRequestRepository extends JpaRepository<TenantRentalRequest, Long> {
    
    List<TenantRentalRequest> findByPropertyTenantEmail(String tenantEmail);

    List<TenantRentalRequest> findByPropertyId(Long propertyId);

    // Obtiene todas las solicitudes de arriendo de un arrendatario por medio de su email
    @Query("SELECT t FROM TenantRentalRequest t WHERE t.requester.email = :email ORDER BY t.requestDateTime DESC")
    List<TenantRentalRequest> findAllRentalRequestsByEmail(@Param("email") String email);

}
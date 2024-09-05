package com.gossip.arrienda_tu_finca.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gossip.arrienda_tu_finca.entities.Payment;
import com.gossip.arrienda_tu_finca.dto.PaymentDTO;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>  {

    // Encontrar Payment de acuerdo a su ID
    @Query("SELECT new com.gossip.arrienda_tu_finca.dto.PaymentDTO(p.id, p.rentalPrice, p.bank, p.accountNumber, p.rentalRequest) FROM Property p WHERE p.id = :id")
    PaymentDTO findPaymentDTOById(@Param("id") Long id);

    // Encontrar Payment de acuerdo a su TenantRentalRequest
    @Query("SELECT p FROM Payment p WHERE p.rentalRequest.id = :rentalRequestId")
    Optional<Payment> findByRentalRequestId(@Param("rentalRequestId") Long rentalRequestId);
}

    



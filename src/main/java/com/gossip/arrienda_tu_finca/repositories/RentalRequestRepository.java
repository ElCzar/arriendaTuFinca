package com.gossip.arrienda_tu_finca.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gossip.arrienda_tu_finca.entities.Comment;
import com.gossip.arrienda_tu_finca.entities.RentalRequest;

@Repository
public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long> {
    @Query("SELECT r FROM RentalRequest r WHERE r.property.owner.email = :email")
    List<RentalRequest> findByHostEmail(String hostEmail);
    @Query("SELECT r FROM RentalRequest r WHERE r.requester.email = :email")
    List<RentalRequest> findByRenterEmail(String renterEmail);
    @Query("SELECT r FROM RentalRequest r WHERE r.property.id = :propertyId")
    List<RentalRequest> findByPropertyId(Long propertyId);

    @Query("SELECT r.hostComment FROM RentalRequest r WHERE r.property.owner.email = :email")
    List<Comment> findCommentsByHostEmail(String hostEmail);
    @Query("SELECT r.renterComment FROM RentalRequest r WHERE r.requester.email = :email")
    List<Comment> findCommentsByRenterEmail(String renterEmail);
    @Query("SELECT r.propertyComment FROM RentalRequest r WHERE r.property.id = :propertyId")
    List<Comment> findCommentsByPropertyId(Long propertyId);
    
    @Query("SELECT r FROM RentalRequest r WHERE r.requester.email = :email ORDER BY r.requestDateTime DESC")
    List<RentalRequest> findByRequesterEmailOrderByRequestDateTime(@Param("email") String requesterEmail);
}
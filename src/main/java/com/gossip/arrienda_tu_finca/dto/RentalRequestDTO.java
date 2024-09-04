package com.gossip.arrienda_tu_finca.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RentalRequestDTO {

    private Long id;
	private Long propertyId;
	private String requesterEmail;
	private LocalDateTime requestDateTime;
	private LocalDate arrivalDate;
	private LocalDate departureDate;
	private Double amount;
	private boolean accepted;
	private boolean rejected;
	private boolean canceled;
	private boolean paid;
	private boolean reviewed;
	private boolean completed;
	private boolean approved;

	// Constructor
    
	public RentalRequestDTO(Long id, Long propertyId, String requesterEmail, LocalDateTime requestDateTime, LocalDate arrivalDate, LocalDate departureDate, Double amount, boolean accepted, boolean rejected, boolean canceled, boolean paid, boolean reviewed, boolean completed, boolean approved) {
		this.id = id;
		this.propertyId = propertyId;
		this.requesterEmail = requesterEmail;
		this.requestDateTime = requestDateTime;
		this.arrivalDate = arrivalDate;
		this.departureDate = departureDate;
		this.amount = amount;
		this.accepted = accepted;
		this.rejected = rejected;
		this.canceled = canceled;
		this.paid = paid;
		this.reviewed = reviewed;
		this.completed = completed;
		this.approved = approved;
	}

	// Getters y Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}

	public String getRequesterEmail() {
		return requesterEmail;
	}

	public void setRequesterEmail(String requesterEmail) {
		this.requesterEmail = requesterEmail;
	}

	public LocalDateTime getRequestDateTime() {
		return requestDateTime;
	}

	public void setRequestDateTime(LocalDateTime requestDateTime) {
		this.requestDateTime = requestDateTime;
	}

	public LocalDate getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(LocalDate arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public LocalDate getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(LocalDate departureDate) {
		this.departureDate = departureDate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public boolean isRejected() {
		return rejected;
	}

	public void setRejected(boolean rejected) {
		this.rejected = rejected;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public boolean isReviewed() {
		return reviewed;
	}

	public void setReviewed(boolean reviewed) {
		this.reviewed = reviewed;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

}

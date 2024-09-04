package com.gossip.arrienda_tu_finca.mapper;

import com.gossip.arrienda_tu_finca.dto.RentalRequestDto;
import com.gossip.arrienda_tu_finca.entities.RentalRequest;

public class RentalRequestMapper {

	public static RentalRequestDto toDTO(RentalRequest rentalRequest) {
		return new RentalRequestDto(
			rentalRequest.getId(),
			rentalRequest.getProperty().getId(),
			rentalRequest.getRequester().getEmail(),
			rentalRequest.getRequestDateTime(),
			rentalRequest.getArrivalDate(),
			rentalRequest.getDepartureDate(),
			rentalRequest.getAmount(),
			rentalRequest.isAccepted(),
			rentalRequest.isRejected(),
			rentalRequest.isCanceled(),
			rentalRequest.isPaid(),
			rentalRequest.isReviewed(),
			rentalRequest.isCompleted(),
			rentalRequest.isApproved()
		);
	}
}

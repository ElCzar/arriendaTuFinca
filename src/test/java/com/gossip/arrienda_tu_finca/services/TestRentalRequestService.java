package com.gossip.arrienda_tu_finca.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gossip.arrienda_tu_finca.ArriendaTuFincaApplication;
import com.gossip.arrienda_tu_finca.entities.Property;
import com.gossip.arrienda_tu_finca.entities.RentalRequest;
import com.gossip.arrienda_tu_finca.entities.User;
import com.gossip.arrienda_tu_finca.exceptions.RentalRequestNotFoundException;
import com.gossip.arrienda_tu_finca.repositories.PropertyRepository;
import com.gossip.arrienda_tu_finca.repositories.RentalRequestRepository;
import com.gossip.arrienda_tu_finca.repositories.UserRepository;

import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = ArriendaTuFincaApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(
    locations = "classpath:application-test.properties"
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class TestRentalRequestService {
    @Autowired
    private RentalRequestService rentalRequestService;

    @Autowired
    private RentalRequestRepository rentalRequestRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DirtiesContext
    @Transactional
    @Description("Given a rental request, when the request is saved, then the request is saved in the database")
    void givenValidPropertyId_whenGetAllPropertyRequest_thenFetchAllPropertyRequests(){
        // Given
        User user = new User();
        user.setEmail("example@example.com");
        user.setPassword("123456");
        userRepository.save(user);

        Property property = new Property();
        property.setOwner(user);
        property.setAddress("Calle 123");
        property.setDescription("Casa campestre");
        propertyRepository.save(property);

        RentalRequest rentalRequest = new RentalRequest();
        rentalRequest.setProperty(property);
        rentalRequestRepository.save(rentalRequest);

        // When
        List<RentalRequest> rentalRequests = rentalRequestService.getRequestsByProperty(1L);

        // Then
        assertEquals(1, rentalRequests.size());
    }

    @Test
    @DirtiesContext
    @Transactional
    @Description("Given an user email, retrieve all rental requests for that user")
    void givenValidUserEmail_whenGetAllUserRequest_thenFetchAllUserRequests(){
        // Given
        User user = new User();
        user.setEmail("example@example.com");
        user.setPassword("123456");
        userRepository.save(user);

        Property property = new Property();
        property.setOwner(user);
        property.setAddress("Calle 123");
        property.setDescription("Casa campestre");
        propertyRepository.save(property);

        RentalRequest rentalRequest = new RentalRequest();
        rentalRequest.setProperty(property);
        rentalRequestRepository.save(rentalRequest);

        // When
        List<RentalRequest> rentalRequests = rentalRequestService.getRequestsByOwner("example@example.com");

        // Then
        assertEquals(1, rentalRequests.size());
    }

    @Test
    @DirtiesContext
    @Transactional
    @Description("Given an owner has no requests for a property should return a NOT FOUND status")
    void givenValidUserEmailButNoRequest_whenGetRequestByOwner_thenReceiveNotFounError(){
        // Given
        User user = new User();
        user.setEmail("example@example.com");
        user.setPassword("123456");
        userRepository.save(user);

        // When and Then
        assertThrows(RentalRequestNotFoundException.class, () -> rentalRequestService.getRequestsByOwner("example@example.com"));
    }

    @Test
    @Transactional
    @Description("Given a valid request ID, when acceptRequest is called, then the request should be marked as accepted")
    void givenValidRequestId_whenAcceptRequest_thenRequestShouldBeAccepted() {
        // Given
        RentalRequest request = new RentalRequest();
        request = rentalRequestRepository.save(request);

        // When
        rentalRequestService.acceptRequest(request.getId());

        // Then
        RentalRequest updatedRequest = rentalRequestRepository.findById(request.getId()).orElseThrow();
        assertTrue(updatedRequest.isAccepted());
    }

    @Test
    @Transactional
    @Description("Given an invalid request ID, when acceptRequest is called, then RentalRequestNotFoundException should be thrown")
    void givenInvalidRequestId_whenAcceptRequest_thenThrowRentalRequestNotFoundException() {
        // Given
        Long invalidId = 999L;

        // When & Then
        assertThrows(RentalRequestNotFoundException.class, () -> rentalRequestService.acceptRequest(invalidId));
    }

    @Test
    @Transactional
    @Description("Given a valid request ID, when cancelRequest is called, then the request should be marked as canceled")
    void givenValidRequestId_whenCancelRequest_thenRequestShouldBeCanceled() {
        // Given
        RentalRequest request = new RentalRequest();
        request = rentalRequestRepository.save(request);

        // When
        rentalRequestService.cancelRequest(request.getId());

        // Then
        RentalRequest updatedRequest = rentalRequestRepository.findById(request.getId()).orElseThrow();
        assertTrue(updatedRequest.isCanceled());
    }

    @Test
    @Transactional
    @Description("Given a valid request ID, when reviewRenter is called, then the request should be marked as reviewed")
    void givenValidRequestId_whenReviewRenter_thenRequestShouldBeReviewed() {
        // Given
        RentalRequest request = new RentalRequest();
        request = rentalRequestRepository.save(request);

        // When
        rentalRequestService.reviewRenter(request.getId());

        // Then
        RentalRequest updatedRequest = rentalRequestRepository.findById(request.getId()).orElseThrow();
        assertTrue(updatedRequest.isReviewed());
    }

    @Test
    @Transactional
    @Description("Given a valid request ID, when completeRequest is called, then the request should be marked as completed")
    void givenValidRequestId_whenCompleteRequest_thenRequestShouldBeCompleted() {
        // Given
        RentalRequest request = new RentalRequest();
        request = rentalRequestRepository.save(request);

        // When
        rentalRequestService.completeRequest(request.getId());

        // Then
        RentalRequest updatedRequest = rentalRequestRepository.findById(request.getId()).orElseThrow();
        assertTrue(updatedRequest.isCompleted());
    }

    @Test
    @Transactional
    @Description("Given a valid request ID, when rejectRequest is called, then the request should be marked as rejected")
    void givenValidRequestId_whenRejectRequest_thenRequestShouldBeRejected() {
        // Given
        RentalRequest request = new RentalRequest();
        request = rentalRequestRepository.save(request);

        // When
        rentalRequestService.rejectRequest(request.getId());

        // Then
        RentalRequest updatedRequest = rentalRequestRepository.findById(request.getId()).orElseThrow();
        assertTrue(updatedRequest.isRejected());
    }

    @Test
    @Transactional
    @Description("Given a valid request ID, when approveRequest is called, then the request should be marked as approved")
    void givenValidRequestId_whenApproveRequest_thenRequestShouldBeApproved() {
        // Given
        RentalRequest request = new RentalRequest();
        request = rentalRequestRepository.save(request);

        // When
        RentalRequest approvedRequest = rentalRequestService.approveRequest(request.getId());

        // Then
        assertTrue(approvedRequest.isApproved());
        RentalRequest updatedRequest = rentalRequestRepository.findById(request.getId()).orElseThrow();
        assertTrue(updatedRequest.isApproved());
    }

    @Test
    @Transactional
    @Description("Given a valid request ID, when payRequest is called, then the request should be marked as paid")
    void givenValidRequestId_whenPayRequest_thenRequestShouldBePaid() {
        // Given
        RentalRequest request = new RentalRequest();
        request = rentalRequestRepository.save(request);

        // When
        RentalRequest paidRequest = rentalRequestService.payRequest(request.getId());

        // Then
        assertTrue(paidRequest.isPaid());
        RentalRequest updatedRequest = rentalRequestRepository.findById(request.getId()).orElseThrow();
        assertTrue(updatedRequest.isPaid());
    }
}

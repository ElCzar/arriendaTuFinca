package com.gossip.arrienda_tu_finca.services;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalDate;

import com.gossip.arrienda_tu_finca.repositories.PropertyRepository;
import com.gossip.arrienda_tu_finca.repositories.RentalRequestRepository;
import com.gossip.arrienda_tu_finca.repositories.UserRepository;
import com.gossip.arrienda_tu_finca.repositories.CommentRepository;
import com.gossip.arrienda_tu_finca.dto.CommentDTO;
import com.gossip.arrienda_tu_finca.dto.RentalRequestCreateDTO;
import com.gossip.arrienda_tu_finca.dto.RentalRequestDto;
import com.gossip.arrienda_tu_finca.entities.Comment;
import com.gossip.arrienda_tu_finca.entities.Property;
import com.gossip.arrienda_tu_finca.entities.RentalRequest;
import com.gossip.arrienda_tu_finca.entities.User;
import com.gossip.arrienda_tu_finca.exceptions.InvalidAmountOfResidentsException;
import com.gossip.arrienda_tu_finca.exceptions.InvalidDateException;
import com.gossip.arrienda_tu_finca.exceptions.InvalidPaymentException;
import com.gossip.arrienda_tu_finca.exceptions.InvalidReviewException;
import com.gossip.arrienda_tu_finca.exceptions.PropertyNotFoundException;
import com.gossip.arrienda_tu_finca.exceptions.RentalRequestNotFoundException;

@Service
public class RentalRequestService {

    private final RentalRequestRepository rentalRequestRepository;
    private final ModelMapper modelMapper;
    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private PropertyRepository propertyRepository;
    private static final String RENTAL_REQUEST_NOT_FOUND = "Solicitud de arriendo no encontrada";

    @Autowired
    public RentalRequestService(RentalRequestRepository rentalRequestRepository, ModelMapper modelMapper, UserRepository userRepository, PropertyRepository propertyRepository, CommentRepository commentRepository) {
        this.rentalRequestRepository = rentalRequestRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.commentRepository = commentRepository;
    }

    /**
     * Creates a rental request from a renter to a property
     * @param propertyId
     * @param rentalRequest
     * @throws PropertyNotFoundException
     * @throws InvalidRenterException 
     */
    public void createRequest(Long propertyId, RentalRequestCreateDTO rentalRequest) {
        RentalRequest request = modelMapper.map(rentalRequest, RentalRequest.class);

        Long userId = userRepository.findIdByEmail(rentalRequest.getRequesterEmail());
        if (userId == null) {
            throw new PropertyNotFoundException("Usuario con email " + rentalRequest.getRequesterEmail() + " mo fue encontrado");
        }
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Propiedad con ID " + propertyId + " no fue encontrada"));
        LocalDate today = LocalDate.now();
        if (rentalRequest.getArrivalDate().isBefore(today)) {
            throw new InvalidDateException("La fecha inicial no puede ser anterior a la fecha actual");
        }
        if (rentalRequest.getDepartureDate().isBefore(rentalRequest.getArrivalDate().plusDays(1))) {
            throw new InvalidDateException("La fecha final no puede ser anterior a un día posterior a la fecha inicial");
        }
        if (rentalRequest.getAmountOfResidents() > property.getAmountOfResidents()) {
            throw new InvalidAmountOfResidentsException("La cantidad de residentes no puede ser superior a la permitida en la propiedad");
        }

        Optional<User> renter = userRepository.findById(userId);
        if (!renter.isPresent()) {
            throw new PropertyNotFoundException("Usuario con ID " + userId + " no fue encontrado");
        }

        request.setProperty(property);
        request.setRequester(renter.get());
        request.setRequestDateTime(LocalDateTime.now()); 
        request.setRejected(false);
        request.setCanceled(false); 
        request.setPaid(false);
        request.setCompleted(false); 
        request.setApproved(false);
        request.setExpired(false);
        rentalRequestRepository.save(request);
    }

    /**
     * Gets all rental requests given the property ID
     * @param propertyId ID of the property
     * @throws RentalRequestNotFoundException if no request were found
     * @return List<RentalRequest> of the request about the property
     */
    public List<RentalRequestDto> getRequestsByProperty(Long propertyId) {
        List<RentalRequest> requests = rentalRequestRepository.findByPropertyId(propertyId);
        if (requests.isEmpty()) {
            throw new RentalRequestNotFoundException("No se encontraron solicitudes de arriendo para la propiedad con ID: " + propertyId);
        }
        return requests.stream().map(request -> modelMapper.map(request, RentalRequestDto.class)).toList();
    }

    /**
     * Gets all rental request given the host email
     * @param hostEmail email of the host
     * @return
     */
    public List<RentalRequestDto> getRequestsByHost(String hostEmail) {
        List<RentalRequest> requests = rentalRequestRepository.findByHostEmail(hostEmail);
        if (requests.isEmpty()) {
            throw new RentalRequestNotFoundException("No se encontraron solicitudes de arriendo para el propietario con email: " + hostEmail);
        }
        return requests.stream().map(request -> modelMapper.map(request, RentalRequestDto.class)).toList();
    }

    /**
     * Gets all the rental request given the renter email
     * @param renterEmail email of the renter
     * @return
     */
    public List<RentalRequestDto> getRequestsByRenter(String renterEmail) {
        List<RentalRequest> requests = rentalRequestRepository.findByRenterEmail(renterEmail);
        if (requests.isEmpty()) {
            throw new RentalRequestNotFoundException("No se encontraron solicitudes de arriendo para el arrendatario con email: " + renterEmail);
        }
        return requests.stream().map(request -> modelMapper.map(request, RentalRequestDto.class)).toList();
    }

    /**
     * Renter cancels the rental request given the request ID
     * @param requestId
     */
    public void cancelRequest(Long requestId) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RentalRequest request = optionalRequest.get();
            
            request.setCanceled(true);
            rentalRequestRepository.save(request);
        } else {
            throw new RentalRequestNotFoundException(RENTAL_REQUEST_NOT_FOUND);
        }
    }

    /**
     * Host completes the rental request given the request ID
     * @param requestId
     */
    public void completeRequest(Long requestId) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RentalRequest request = optionalRequest.get();
            request.setCompleted(true);
            rentalRequestRepository.save(request);
        } else {
            throw new RentalRequestNotFoundException(RENTAL_REQUEST_NOT_FOUND);
        }
    }

    /**
     * Host rejects the rental request given the request ID
     * @param requestId
     */
    public void rejectRequest(Long requestId) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RentalRequest request = optionalRequest.get();
            request.setRejected(true);
            rentalRequestRepository.save(request);
        } else {
            throw new RentalRequestNotFoundException(RENTAL_REQUEST_NOT_FOUND);
        }
    }

    /**
     * Host approve the rental request given the request ID
     * @param requestId
     * @return
     */
    public RentalRequest approveRequest(Long requestId) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RentalRequest request = optionalRequest.get();
            request.setApproved(true);
            rentalRequestRepository.save(request);
            return request;
        } else {
            throw new RentalRequestNotFoundException(RENTAL_REQUEST_NOT_FOUND);
        }
    }

    /**
     * Renter pays the rental request given the request ID
     * @param requestId
     * @return
     */
    public RentalRequest payRequest(Long requestId) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RentalRequest request = optionalRequest.get();
            if (!request.isApproved()) {
                throw new InvalidPaymentException("La solicitud de arriendo no ha sido aceptada.");
             }
             if (request.isPaid()) {
                throw new InvalidPaymentException("La solicitud de arriendo ya ha sido pagada.");
             }
             if (request.isExpired()) {
                 throw new InvalidPaymentException("La solicitud de arriendo ha expirado.");
             }
            request.setPaid(true);
            rentalRequestRepository.save(request);
            return request;
        } else {
            throw new RentalRequestNotFoundException(RENTAL_REQUEST_NOT_FOUND);
        }
    }

    /**
     * Checks if the renter comment is valid and if the renter is correct for the request id
     * @param commentDto
     * @return
     */
    private Comment isRenterCommentValid(RentalRequest rentalRequest, CommentDTO commentDto) {
        if(commentDto.getContent() == null || commentDto.getContent().isEmpty()) {
            throw new InvalidReviewException("El comentario no puede estar vacío.");
        }
        if(commentDto.getRating() < 1 || commentDto.getRating() > 5) {
            throw new InvalidReviewException("La calificación debe estar entre 1 y 5.");
        }
        String renterEmail = rentalRequest.getRequester().getEmail();
        if (!renterEmail.equals(commentDto.getAuthorEmail())) {
            throw new InvalidReviewException("El arrendatario no coincide con el autor del comentario.");
        }
        return modelMapper.map(commentDto, Comment.class);
    }

    /**
     * Renter reviews the property given the request ID
     * @param requestId
     * @param commentDto
     * @return
     */
    public void reviewProperty(Long requestId, CommentDTO commentDto) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (!optionalRequest.isPresent()) {
            throw new RentalRequestNotFoundException(RENTAL_REQUEST_NOT_FOUND);
        }
        RentalRequest request = optionalRequest.get();
        if (!request.isPaid()) {
            throw new InvalidReviewException("La solicitud de arriendo no ha sido pagada.");
        }
        Comment comment = isRenterCommentValid(request, commentDto);
        Comment databaseComment = commentRepository.save(comment);
        request.setPropertyComment(databaseComment);
        rentalRequestRepository.save(request);
        updatePropertyRating(request.getProperty().getId());
    }

    /**
     * The rating for the property is updated
     * @param propertyId
     * @return
     */
    private void updatePropertyRating(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException("Propiedad con ID " + propertyId + " no fue encontrada"));
        List<Comment> requests = rentalRequestRepository.findCommentsByPropertyId(propertyId);
        double rating = 0;
        for (Comment request : requests) {
            rating += request.getRating();
        }
        rating = rating / requests.size();
        property.setRating(rating);
        propertyRepository.save(property);
    }

    /**
     * Renter reviews the host given the request ID
     * @param requestId
     * @param commentDto
     * @return
     */
    public void reviewHost(Long requestId, CommentDTO commentDto) {
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (!optionalRequest.isPresent()) {
            throw new RentalRequestNotFoundException(RENTAL_REQUEST_NOT_FOUND);
        }
        RentalRequest request = optionalRequest.get();
        if (!request.isPaid()) {
            throw new InvalidReviewException("La solicitud de arriendo no ha sido pagada.");
        }
        Comment comment = isRenterCommentValid(request, commentDto);
        Comment databaseComment = commentRepository.save(comment);
        request.setHostComment(databaseComment);
        rentalRequestRepository.save(request);
        updateHostRating(request);
    }

    /**
     * The rating for host is updated
     * @param requestId
     * @return
     */
    private void updateHostRating(RentalRequest request) {
        User host = request.getProperty().getOwner();
        List<Comment> requests = rentalRequestRepository.findCommentsByHostEmail(host.getEmail());
        double rating = 0;
        for (Comment comment : requests) {
            rating += comment.getRating();
        }
        rating = rating / requests.size();
        host.setRatingHost(rating);
        userRepository.save(host);
    }

    /**
     * Checks it the comment is valid and if the host is correct for the request id
     * @param requestId
     * @param commentDto
     * @return Comment
     */
    private Comment isHostCommentValid(Long requestId, CommentDTO commentDto) {
        if(commentDto.getContent() == null || commentDto.getContent().isEmpty()) {
            throw new InvalidReviewException("El comentario no puede estar vacío.");
        }
        if(commentDto.getRating() < 1 || commentDto.getRating() > 5) {
            throw new InvalidReviewException("La calificación debe estar entre 1 y 5.");
        }
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (!optionalRequest.isPresent()) {
            throw new RentalRequestNotFoundException(RENTAL_REQUEST_NOT_FOUND);
        }
        RentalRequest request = optionalRequest.get();
        String hostEmail = request.getProperty().getOwner().getEmail();
        if (!hostEmail.equals(commentDto.getAuthorEmail())) {
            throw new InvalidReviewException("El propietario no coincide con el autor del comentario.");
        }
        return modelMapper.map(commentDto, Comment.class);
    }

    /**
     * Host reviews the renter given the request ID
     * @param requestId
     * @param commentDto
     * @return
     */
    public void reviewRenter(Long requestId, CommentDTO commentDto) {
        Comment comment = isHostCommentValid(requestId, commentDto);
        Comment databaseComment = commentRepository.save(comment);
        Optional<RentalRequest> optionalRequest = rentalRequestRepository.findById(requestId);
        if (!optionalRequest.isPresent()) {
            throw new RentalRequestNotFoundException(RENTAL_REQUEST_NOT_FOUND);
        }
        RentalRequest request = optionalRequest.get();
        request.setRenterComment(databaseComment);
        rentalRequestRepository.save(request);
        updateRenterRating(request);
    }

    /**
     * The rating for the renter is updated
     * @param RentalRequest
     * @return
     */
    private void updateRenterRating(RentalRequest request) {
        User renter = request.getRequester();
        List<Comment> requests = rentalRequestRepository.findCommentsByRenterEmail(renter.getEmail());
        double rating = 0;
        for (Comment comment : requests) {
            rating += comment.getRating();
        }
        rating = rating / requests.size();
        renter.setRatingRenter(rating);
        userRepository.save(renter);
    }

    /**
     * Gets all the comments that rate the renter given the renter email
     * @param email
     * @return List<CommentDTO>
     */
    public List<CommentDTO> getRenterComments(String email) {
        List<Comment> comments = rentalRequestRepository.findCommentsByRenterEmail(email);
        return comments.stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).toList();
    }

    /**
     * Gets all the comments that rate the host given the host email
     * @param email
     * @return List<CommentDTO>
     */
    public List<CommentDTO> getHostComments(String email) {
        List<Comment> comments = rentalRequestRepository.findCommentsByHostEmail(email);
        return comments.stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).toList();
    }

    /**
     * Gets all the comments that rate the property given the property ID
     * @param propertyId
     * @return List<CommentDTO>
     */
    public List<CommentDTO> getPropertyComments(Long propertyId) {
        List<Comment> comments = rentalRequestRepository.findCommentsByPropertyId(propertyId);
        return comments.stream().map(comment -> modelMapper.map(comment, CommentDTO.class)).toList();
    } 
}
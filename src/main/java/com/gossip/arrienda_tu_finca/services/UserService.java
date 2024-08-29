package com.gossip.arrienda_tu_finca.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.gossip.arrienda_tu_finca.ModelMapper;
import com.gossip.arrienda_tu_finca.entities.User;
import com.gossip.arrienda_tu_finca.exceptions.UserNotValidException;
import com.gossip.arrienda_tu_finca.repositories.UserRepository;

public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncryptionService passwordEncryptionService;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncryptionService passwordEncryptionService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncryptionService = passwordEncryptionService;
    }

    /**
     * Creates a new user after checking if all the fields are valid
     * @param user
     */
    public void createUser(User user) {
        isUserValid(user);
        isNewUserEmailAndPasswordValid(user.getEmail(), user.getPassword());
        user.setPassword(passwordEncryptionService.encryptPassword(user.getPassword()));
        userRepository.save(user);
    }

    /**
     * Checks if the user is valid by checking if all the fields are valid
     * @param user
     * @throws UserNotValidException if the user is not valid with a message explaining why
     * @return true if the user is valid
     */
    private boolean isUserValid(User user) throws UserNotValidException {
        if (user == null) {
            throw new UserNotValidException("User is null");
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getSurname() == null || user.getSurname().isEmpty()) {
            throw new UserNotValidException("Name/Surname is null or empty");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new UserNotValidException("Password is null or empty");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new UserNotValidException("Email is null or empty");
        }
        if (user.getPhone() == null || user.getPhone().isEmpty()) {
            throw new UserNotValidException("Phone is null or empty");
        }
        if (!user.isHost() && !user.isRenter()) {
            throw new UserNotValidException("User is neither host nor renter");
        }

        return true;
    }

    /**
     * Checks if the user is valid vy checking if the email and password are valid
     * @param email
     * @param password
     * @throws UserNotValidException if the user is not valid with a message explaining why
     * @return true if the user is valid
     */
    private boolean isNewUserEmailAndPasswordValid(String email, String password) throws UserNotValidException {
        if (userRepository.existsById(email)) {
            throw new UserNotValidException("Email is already in use");
        }
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new UserNotValidException("Email is not valid");
        }
        if (password.length() < 8 || !password.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,}$")) {
            throw new UserNotValidException("Password is not valid");
        }

        return true;
    }

    
}

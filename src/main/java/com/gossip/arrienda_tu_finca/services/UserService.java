package com.gossip.arrienda_tu_finca.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gossip.arrienda_tu_finca.dto.ChangePasswordDTO;
import com.gossip.arrienda_tu_finca.dto.LoginDTO;
import com.gossip.arrienda_tu_finca.dto.UserDTO;
import com.gossip.arrienda_tu_finca.dto.UserInfoDTO;
import com.gossip.arrienda_tu_finca.entities.User;
import com.gossip.arrienda_tu_finca.exceptions.UserNotFoundException;
import com.gossip.arrienda_tu_finca.exceptions.UserNotValidException;
import com.gossip.arrienda_tu_finca.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncryptionService passwordEncryptionService;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncryptionService = new PasswordEncryptionService();
    }

    /**
     * Gets the information of a user
     * @param userId
     * @throws UserNotFoundException if the user is not found
     * @return UserInfoDTO with the information of the user
     */
    public UserInfoDTO getUserInfo(Long userId) {
        UserInfoDTO user = userRepository.findUserInfoDTOById(userId);

        if (user == null) {
            throw new UserNotFoundException("User information not found by id: " + userId);
        }

        return user;
    }

    /**
     * Makes login with the user's email and password
     * @param userDTO
     * @throws UserNotValidException if the user or password is not valid
     * TODO change function to create a session and a token for the user
     */
    public Long login(LoginDTO loginDTO) throws UserNotValidException {
        User user = modelMapper.map(loginDTO, User.class);

        if (user == null) {
            throw new UserNotValidException("User has no information for login");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new UserNotValidException("Email is null or empty for login");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new UserNotValidException("Password is null or empty for login");
        }

        LoginDTO userFromDB = userRepository.findLoginDTOByEmail(user.getEmail());

        if (userFromDB == null || !passwordEncryptionService.checkPassword(user.getPassword(), userFromDB.getPassword())) {
            throw new UserNotValidException("Email or password is incorrect");
        }

        User uesrRepository = userRepository.findByEmail(user.getEmail());
        return uesrRepository.getId();
    }

    /**
     * Creates a new user after checking if all the fields are valid
     * @param user
     * @throws UserNotValidException if the user is not valid with a message explaining why
     */
    public void createUser(UserDTO userDTO) throws UserNotValidException {
        User user = modelMapper.map(userDTO, User.class);
        
        isUserValid(user);
        isUserEmailAndPasswordValid(user.getEmail(), user.getPassword());

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserNotValidException("Email is already in use");
        }

        user.setPassword(passwordEncryptionService.encryptPassword(user.getPassword()));
        userRepository.save(user);
    }

    /**
     * Updates an existing user after checking if all the fields are valid and email is already in use
     * @param userInfoDTO
     * @param userId
     * @throws UserNotValidException if the user is not valid with a message explaining why
     * @throws UserNotFoundException if the user is not found
     */
    public void updateUser(UserInfoDTO userInfoDTO, Long userId) throws UserNotValidException, UserNotFoundException {
        User user = modelMapper.map(userInfoDTO, User.class);
        isUserValid(user);

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User for update not found by id: " + userId);
        }

        userRepository.save(user);
    }

    /**
     * Updates an existing user's password after checking if the password is valid
     * @param changePasswordDTO
     * @param userId
     * @throws UserNotValidException if the password is not valid with a message explaining why
     * @throws UserNotFoundException if the user is not found
     */
    public void updatePassword(ChangePasswordDTO changePasswordDTO, Long userId) throws UserNotValidException, UserNotFoundException {
        if (changePasswordDTO == null) {
            throw new UserNotValidException("Body for password change is empty");
        }
        if (changePasswordDTO.getNewPassword() == null || changePasswordDTO.getOldPassword() == null ||changePasswordDTO.getOldPassword().isEmpty() || changePasswordDTO.getNewPassword().isEmpty()) {
            throw new UserNotValidException("Passwords for password change are null or empty");
        }
        if (changePasswordDTO.getEmail() == null || changePasswordDTO.getEmail().isEmpty()) {
            throw new UserNotValidException("Email for password change is null or empty");
        }

        // Just get the information for a login dto from the repository
        LoginDTO userFromDB = userRepository.findLoginDTOByEmail(changePasswordDTO.getEmail());

        if (userFromDB == null) {
            throw new UserNotFoundException("User for password update not found by id: " + userId);
        }
        if (!userFromDB.getEmail().equals(changePasswordDTO.getEmail())) {
            throw new UserNotValidException("Email for password change is incorrect");
        }
        if (!passwordEncryptionService.checkPassword(changePasswordDTO.getOldPassword(), userFromDB.getPassword())) {
            throw new UserNotValidException("Old password for password change is incorrect");
        }
        isUserEmailAndPasswordValid(changePasswordDTO.getEmail(), changePasswordDTO.getNewPassword());
        User user = modelMapper.map(changePasswordDTO, User.class);
        userRepository.save(user);
    }

    /**
     * Deletes an existing user
     * @param loginDTO
     * @param userId
     * @throws UserNotValidException if the user is not valid with a message explaining why
     * @throws UserNotFoundException if the user is not found
     */
    public void deleteUser(LoginDTO loginDTO, Long userId) {
        User user = modelMapper.map(loginDTO, User.class);

        if (user == null) {
            throw new UserNotValidException("User has no information for delete");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new UserNotValidException("Email is null or empty for delete");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new UserNotValidException("Password is null or empty for delete");
        }

        LoginDTO userFromDB = userRepository.findLoginDTOById(userId);

        if (userFromDB == null) {
            throw new UserNotFoundException("User for delete not found by id: " + userId);
        }
        if (!passwordEncryptionService.checkPassword(user.getPassword(), userFromDB.getPassword())) {
            throw new UserNotValidException("Email or password is incorrect for delete");
        }

        userRepository.deleteById(userId);
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
    private boolean isUserEmailAndPasswordValid(String email, String password) throws UserNotValidException {
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new UserNotValidException("Email is not valid with the format");
        }
        if (password == null || password.length() < 8 || !password.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).{8,}$")) {
            throw new UserNotValidException("Password is not valid with at least 8 characters, 1 digit, 1 lowercase, 1 uppercase and 1 special character");
        }

        return true;
    }

    
}

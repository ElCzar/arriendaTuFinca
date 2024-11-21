package com.gossip.arrienda_tu_finca.services;

import java.io.IOException;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gossip.arrienda_tu_finca.dto.ChangePasswordDTO;
import com.gossip.arrienda_tu_finca.dto.ChangeUserInfoDTO;
import com.gossip.arrienda_tu_finca.dto.LoginDTO;
import com.gossip.arrienda_tu_finca.dto.TokenDTO;
import com.gossip.arrienda_tu_finca.dto.UserDTO;
import com.gossip.arrienda_tu_finca.dto.UserInfoDTO;
import com.gossip.arrienda_tu_finca.entities.Image;
import com.gossip.arrienda_tu_finca.entities.User;
import com.gossip.arrienda_tu_finca.exceptions.UserNotFoundException;
import com.gossip.arrienda_tu_finca.exceptions.UserNotValidException;
import com.gossip.arrienda_tu_finca.repositories.ImageRepository;
import com.gossip.arrienda_tu_finca.repositories.UserRepository;
import com.gossip.arrienda_tu_finca.security.CustomUserDetailsService;
import com.gossip.arrienda_tu_finca.security.JWTTokenService;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncryptionService passwordEncryptionService;
    private final ImageRepository imageRepository;
    private final CustomUserDetailsService userDetailsService;
    private final JWTTokenService jwtTokenService;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper, ImageRepository imageRepository, CustomUserDetailsService userDetailsService, JWTTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncryptionService = new PasswordEncryptionService();
        this.imageRepository = imageRepository;
        this.userDetailsService = userDetailsService;
        this.jwtTokenService = jwtTokenService;
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
     * @param loginDTO
     * @throws UserNotValidException if the user or password is not valid
     * @return TokenDTO with the token of the user
     */
    public TokenDTO login(LoginDTO loginDTO) throws UserNotValidException {
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
        Long id = userRepository.findIdByEmail(user.getEmail());
        UserDetails userDetails = userDetailsService.loadByEmail(user.getEmail());
        String token = jwtTokenService.generarToken(userDetails);
        return new TokenDTO(token, id);
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
     * Uploads a photo for a user
     * @param photo
     * @param userId
     * @throws UserNotFoundException
     * @throws IOException
     */
    public void uploadPhoto(MultipartFile photo, Long userId) throws UserNotFoundException, IOException {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User for photo upload not found by id: " + userId);
        }
        int imageId = user.getImageId();
        if (imageId != 0) {
            imageRepository.deleteById(imageId);
        }
        Image image = new Image();
        image.setImageData(photo.getBytes());
        image.setName(photo.getOriginalFilename());
        
        int newImageId = imageRepository.save(image).getId();
        user.setImageId(newImageId);
        userRepository.save(user);
    }


    /**
     * Updates an existing user after checking if all the fields are valid and email is already in use
     * @param userInfoDTO
     * @param userId
     * @throws UserNotValidException if the user is not valid with a message explaining why
     * @throws UserNotFoundException if the user is not found
     */
    public void updateUser(ChangeUserInfoDTO changeUserInfoDTO, Long userId) throws UserNotValidException, UserNotFoundException {
        User user = modelMapper.map(changeUserInfoDTO, User.class);
        
        Optional<User> optionalUserFromDB = userRepository.findById(userId);

        if (optionalUserFromDB.isEmpty()) {
            throw new UserNotFoundException("User for update not found by id: " + userId);
        }

        isUserValid(user);
        User userFromDB = optionalUserFromDB.get();

        if (!userFromDB.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(user.getEmail())) {
            throw new UserNotValidException("Email is already in use");
        }

        userFromDB.setName(user.getName());
        userFromDB.setSurname(user.getSurname());
        userFromDB.setEmail(user.getEmail());
        userFromDB.setPhone(user.getPhone());
        userFromDB.setHost(user.isHost());
        userFromDB.setRenter(user.isRenter());
        userRepository.save(userFromDB);
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

        // Get all information of the user from the database
        User userFromDB = userRepository.findById(userId).orElse(null);

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
        userFromDB.setPassword(passwordEncryptionService.encryptPassword(changePasswordDTO.getNewPassword()));
        userRepository.save(userFromDB);
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

        Optional<LoginDTO> optionalUserFromDB = Optional.ofNullable(userRepository.findLoginDTOById(userId));

        if (optionalUserFromDB.isEmpty()) {
            throw new UserNotFoundException("User for delete not found by id: " + userId);
        }

        if (user == null) {
            throw new UserNotValidException("User has no information for delete");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new UserNotValidException("Email is null or empty for delete");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new UserNotValidException("Password is null or empty for delete");
        }

        LoginDTO userFromDB = optionalUserFromDB.get();
        if (!userFromDB.getEmail().equals(user.getEmail())) {
            throw new UserNotValidException("Email is incorrect for delete");
        }
        if (!passwordEncryptionService.checkPassword(user.getPassword(), userFromDB.getPassword())) {
            throw new UserNotValidException("Password is incorrect for delete");
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

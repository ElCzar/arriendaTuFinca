package com.gossip.arrienda_tu_finca.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gossip.arrienda_tu_finca.dto.ChangePasswordDTO;
import com.gossip.arrienda_tu_finca.dto.ChangeUserInfoDTO;
import com.gossip.arrienda_tu_finca.dto.LoginDTO;
import com.gossip.arrienda_tu_finca.dto.TokenDTO;
import com.gossip.arrienda_tu_finca.dto.UserDTO;
import com.gossip.arrienda_tu_finca.dto.UserInfoDTO;
import com.gossip.arrienda_tu_finca.exceptions.UserNotFoundException;
import com.gossip.arrienda_tu_finca.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Gets the information of a user
     * @param userId
     * @return UserInfoDTO with the information of the user as a JSON
     */
    @GetMapping("/info/{userId}")
    public ResponseEntity<UserInfoDTO> getUserInfo(@PathVariable Long userId) {
        UserInfoDTO userInfoDTO = userService.getUserInfo(userId);
        return ResponseEntity.ok(userInfoDTO);
    }

    /**
     * Makes login with the user's email and password
     * @param loginDTO
     * @return ResponseEntity<Void> with status 200
     */
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) {
        TokenDTO tokenDTO = userService.login(loginDTO);
        return ResponseEntity.ok(tokenDTO);
    }

    /**
     * Creates a new user
     * @param userDTO
     * @return ResponseEntity<Void> with status 200
     */
    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO) {
        userService.createUser(userDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * Uploads a photo for a user
     * @param photo
     * @param userId
     * @return
     * @throws IOException 
     * @throws UserNotFoundException 
     */
    @PostMapping("/uploadPhoto/{userId}")
    public ResponseEntity<Void> uploadPhoto(@RequestParam("photo") MultipartFile photo, @PathVariable Long userId) throws UserNotFoundException, IOException {
        userService.uploadPhoto(photo, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Updates an existing user
     * @param userInfoDTO
     * @return ResponseEntity<Void> with status 200
     */
    @PutMapping("/update/{userId}")
    public ResponseEntity<Void> updateUser(@RequestBody ChangeUserInfoDTO changeUserInfoDTO, @PathVariable Long userId) {
        userService.updateUser(changeUserInfoDTO, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Updates an existing user's password
     * @param ChangePasswordDTO
     * @return ResponseEntity<Void> with status 200
     */
    @PutMapping("/update/{userId}/password")
    public ResponseEntity<Void> updatePassword(@RequestBody ChangePasswordDTO changePasswordDTO, @PathVariable Long userId) {
        userService.updatePassword(changePasswordDTO, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Deletes an existing user
     * @param userDTO
     * @return ResponseEntity<Void> with status 200
     */
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@RequestBody LoginDTO loginDTO, @PathVariable Long userId) {
        userService.deleteUser(loginDTO, userId);
        return ResponseEntity.ok().build();
    }
}

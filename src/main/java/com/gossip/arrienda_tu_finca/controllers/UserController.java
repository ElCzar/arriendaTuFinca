package com.gossip.arrienda_tu_finca.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gossip.arrienda_tu_finca.dto.ChangePasswordDTO;
import com.gossip.arrienda_tu_finca.dto.LoginDTO;
import com.gossip.arrienda_tu_finca.dto.UserDTO;
import com.gossip.arrienda_tu_finca.dto.UserInfoDTO;
import com.gossip.arrienda_tu_finca.services.UserService;

@RestController
@RequestMapping("/api/user")
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
    public ResponseEntity<Long> login(@RequestBody LoginDTO loginDTO) {
        Long userId = userService.login(loginDTO);
        return ResponseEntity.ok(userId);
    }

    /**
     * Creates a new user
     * @param userDTO
     * @return ResponseEntity<Void> with status 200
     */
    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO) {
        System.out.println(userDTO.isHost() + " " + userDTO.isRenter());
        userService.createUser(userDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * Updates an existing user
     * @param userInfoDTO
     * @return ResponseEntity<Void> with status 200
     */
    @PutMapping("/update/{userId}")
    public ResponseEntity<Void> updateUser(@RequestBody UserInfoDTO userInfoDTO, @PathVariable Long userId) {
        userService.updateUser(userInfoDTO, userId);
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
    @PostMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@RequestBody LoginDTO loginDTO, @PathVariable Long userId) {
        userService.deleteUser(loginDTO, userId);
        return ResponseEntity.ok().build();
    }
}

package com.gossip.arrienda_tu_finca.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gossip.arrienda_tu_finca.dto.LoginDTO;
import com.gossip.arrienda_tu_finca.dto.UserInfoDTO;
import com.gossip.arrienda_tu_finca.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOptionalByEmail(String email);
    User findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u.email FROM User u WHERE u.id = :userId")
    String findEmailById(@Param("userId") Long userId);

    @Query("SELECT new com.gossip.arrienda_tu_finca.dto.LoginDTO(u.email, u.password) FROM User u WHERE u.email = :email")
    LoginDTO findLoginDTOByEmail(@Param("email") String email);
    
    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    Long findIdByEmail(@Param("email") String email);

    @Query("SELECT new com.gossip.arrienda_tu_finca.dto.LoginDTO(u.email, u.password) FROM User u WHERE u.id = :userId")
    LoginDTO findLoginDTOById(@Param("userId") Long userId);

    @Query("SELECT new com.gossip.arrienda_tu_finca.dto.UserInfoDTO(u.id, u.email, u.name, u.surname, u.phone, u.isHost, u.ratingHost, u.isRenter, u.ratingRenter, u.imageId) FROM User u WHERE u.id = :userId")
    UserInfoDTO findUserInfoDTOById(@Param("userId") Long userId);
}

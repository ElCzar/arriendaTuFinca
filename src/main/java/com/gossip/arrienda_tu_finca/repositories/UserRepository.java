package com.gossip.arrienda_tu_finca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gossip.arrienda_tu_finca.dto.LoginDTO;
import com.gossip.arrienda_tu_finca.dto.UserInfoDTO;
import com.gossip.arrienda_tu_finca.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u.email, u.password FROM user u WHERE u.email = :email")
    LoginDTO findLoginDTOByEmail(@Param("email") String email); 

    @Query("SELECT u.email, u.password FROM user u WHERE u.id = :userId")
    LoginDTO findLoginDTOById(@Param("userId") Long userId);

    @Query("SELECT u.id, u.email, u.name, u.surname, u.phone, u.isHost, u.isRenter FROM user u WHERE u.id = :userId")
    UserInfoDTO findUserInfoDTOById(Long userId);


}

package com.gossip.arrienda_tu_finca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gossip.arrienda_tu_finca.entities.User;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
}

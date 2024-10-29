package com.gossip.arrienda_tu_finca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gossip.arrienda_tu_finca.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findById(int id);
}

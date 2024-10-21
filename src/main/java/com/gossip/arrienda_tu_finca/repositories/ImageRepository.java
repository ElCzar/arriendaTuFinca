package com.gossip.arrienda_tu_finca.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gossip.arrienda_tu_finca.entities.Image;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findAllByIdIn(List<Integer> ids);
}

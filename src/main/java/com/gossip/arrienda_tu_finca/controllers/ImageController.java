package com.gossip.arrienda_tu_finca.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gossip.arrienda_tu_finca.services.ImageService;

@Controller
@RequestMapping("/image")
public class ImageController {
    private ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * Obtain the image with a given id
     * @param id
     * @return ResponseEntity<byte[]> with the image as a byte array
     */
    @GetMapping(value = "/{id}", produces = "image/jpeg")
    public ResponseEntity<byte[]> getPhoto(@PathVariable int id) {
        byte[] photo = imageService.getPhoto(id);
        return ResponseEntity.ok(photo);
    }
}

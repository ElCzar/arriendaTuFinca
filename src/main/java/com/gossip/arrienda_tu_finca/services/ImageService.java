package com.gossip.arrienda_tu_finca.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gossip.arrienda_tu_finca.entities.Image;
import com.gossip.arrienda_tu_finca.exceptions.ImageNotFoundException;
import com.gossip.arrienda_tu_finca.repositories.ImageRepository;

@Service
public class ImageService {
    private ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    /**
     * Obtain the image with a given id
     * @param id
     * @return byte[] with the image
     */
    public byte[] getPhoto(int id) {
        Image image = imageRepository.findById(id);
        if (image == null) {
            throw new ImageNotFoundException("Image with id " + id + " not found");
        }
        return image.getImageData();
    }
}

package com.gossip.arrienda_tu_finca.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gossip.arrienda_tu_finca.dto.PropertyCreateDTO;
import com.gossip.arrienda_tu_finca.dto.PropertyDTO;
import com.gossip.arrienda_tu_finca.dto.PropertyUpdateDTO;
import com.gossip.arrienda_tu_finca.entities.Image;
import com.gossip.arrienda_tu_finca.entities.Property;
import com.gossip.arrienda_tu_finca.entities.User;
import com.gossip.arrienda_tu_finca.exceptions.PropertyNotFoundException;
import com.gossip.arrienda_tu_finca.repositories.ImageRepository;
import com.gossip.arrienda_tu_finca.repositories.PropertyRepository;
import com.gossip.arrienda_tu_finca.repositories.UserRepository;

@Service
public class PropertyService {
    private PropertyRepository propertyRepository;
    private ModelMapper modelMapper;
    private UserRepository userRepository;
    private ImageRepository imageRepository;
    private static final Logger logger = LoggerFactory.getLogger(PropertyService.class);

    @Autowired
    public PropertyService(PropertyRepository propertyRepository, ModelMapper modelMapper, UserRepository userRepository, ImageRepository imageRepository) {
        this.propertyRepository = propertyRepository;
        this.modelMapper = modelMapper; 
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    /**
     * Creates a property with the information given in the PropertyCreateDTO
     * @param propertyCreateDTO
     * @throws PropertyNotFoundException
     * @return PropertyDTO with the information of the created property
     */
    public PropertyDTO createProperty(PropertyCreateDTO propertyCreateDTO) {
        Property property = modelMapper.map(propertyCreateDTO, Property.class); 
        property.setAvailable(true); 

        Long userId = userRepository.findIdByEmail(propertyCreateDTO.getOwnerEmail());

        if (userId == null) {
            throw new PropertyNotFoundException("User with email " + propertyCreateDTO.getOwnerEmail() + " not found when creating property");
        }

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new PropertyNotFoundException("User with ID " + userId + " not found when creating property");
        }

        property.setOwner(user.get());

        Property savedProperty = propertyRepository.save(property);
        return modelMapper.map(savedProperty, PropertyDTO.class); 
    }

    /**
     * Obtains the information of a property with a given id
     * @param id
     * @throws PropertyNotFoundException
     * @return PropertyDTO with the information of the property
     */
    public PropertyDTO getPropertyById(Long id) {
        Property property = propertyRepository.findById(id)
            .orElseThrow(() -> new PropertyNotFoundException("Property with ID " + id + " not found for fetching"));
        return modelMapper.map(property, PropertyDTO.class);
    }

    /**
     * Obtains the information of all properties
     * @return List<PropertyDTO> with the information of all properties
     */
    public List<PropertyDTO> getAllProperties() {
        List<Property> properties = propertyRepository.findAll();
        return properties.stream()
                .map(property -> modelMapper.map(property, PropertyDTO.class)) // Mapea cada entidad a DTO
                .collect(Collectors.toList());
    }

    /**
     * Update a property with the information given in the PropertyUpdateDTO
     * @param id
     * @param propertyUpdateDTO
     * @throws PropertyNotFoundException
     * @return
    */
    public PropertyDTO updateProperty(Long id, PropertyUpdateDTO propertyUpdateDTO) {
        Property property = propertyRepository.findById(id)
            .orElseThrow(() -> new PropertyNotFoundException("To update property with ID " + id + " not found"));
        modelMapper.map(propertyUpdateDTO, property); 
        Property updatedProperty = propertyRepository.save(property);
        return modelMapper.map(updatedProperty, PropertyDTO.class);
    }

    /**
     * Deactivate a property with a given id
     * @param id
     * @throws PropertyNotFoundException
     */
    public void deactivateProperty(Long id) {
        Property property = propertyRepository.findById(id)
            .orElseThrow(() -> new PropertyNotFoundException("Property to deactivate with ID " + id + " not found"));
        property.setAvailable(false);
        propertyRepository.save(property);
    }
    
    /**
     * Upload a photo to a property
     * @param id
     * @param photo
     * @throws IOException
     */
    public void uploadPhoto(Long id, MultipartFile photo) throws IOException {
        Property property = propertyRepository.findById(id)
            .orElseThrow(() -> new PropertyNotFoundException("Property not found"));
        
        Image image = new Image();
        image.setName(photo.getOriginalFilename());
        image.setImageData(photo.getBytes());
        int imageId = imageRepository.save(image).getId();
        logger.info("Image with ID {} uploaded", imageId);
        if (property.getImageIds() == null) {
            property.setImageIds(String.valueOf(imageId));
        } else {
            property.setImageIds(property.getImageIds() + "," + imageId);
        }
        propertyRepository.save(property);
    }

    // Arrendatario
    // Obtener todas las propiedades de un municipio aleatorio
    public List<PropertyDTO> findPropertiesByRandomMunicipality() {
        String randomMunicipality = propertyRepository.findRandomMunicipality();
        List<Property> properties = propertyRepository.findPropertiesByMunicipality(randomMunicipality);
        return properties.stream()
                .map(property -> modelMapper.map(property, PropertyDTO.class)) 
                .collect(Collectors.toList());
    }

    // Obtener todas las propiedades con un nombre especifico
    public List<PropertyDTO> findPropertiesByName(String name) {
        List<Property> properties = propertyRepository.findPropertiesByName(name);
        if (properties.isEmpty()) {
            throw new PropertyNotFoundException("Propiedades no encontradas con el nombre " + name);
        }
        return properties.stream()
                .map(property -> modelMapper.map(property, PropertyDTO.class)) 
                .collect(Collectors.toList());
    }

    // Obtener todas las propiedades de un municipio especifico
    public List<PropertyDTO> findPropertiesByMunicipality(String municipality) {
        List<Property> properties = propertyRepository.findPropertiesByMunicipality(municipality);
        if (properties.isEmpty()) {
            throw new PropertyNotFoundException("Propiedades del municipio " + municipality + " no fueron encontradas");
        }
        return properties.stream()
                .map(property -> modelMapper.map(property, PropertyDTO.class)) 
                .collect(Collectors.toList());
    }

    // Obtener todas las propiedades con una cantidad de residentes especifica
    public List<PropertyDTO> findPropertiesByAmountOfResidents(Integer amountOfResidents) {
        List<Property> properties = propertyRepository.findPropertiesByAmountOfResidents(amountOfResidents);
        if (properties.isEmpty()) {
            throw new PropertyNotFoundException("Propiedades con cantidad de residentes " + amountOfResidents + " no fueron encontradas");
        }
        return properties.stream()
                .map(property -> modelMapper.map(property, PropertyDTO.class)) 
                .collect(Collectors.toList());
    }
}

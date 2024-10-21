package com.gossip.arrienda_tu_finca.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gossip.arrienda_tu_finca.dto.PropertyCreateDTO;
import com.gossip.arrienda_tu_finca.dto.PropertyDTO;
import com.gossip.arrienda_tu_finca.dto.PropertyUpdateDTO;
import com.gossip.arrienda_tu_finca.entities.Image;
import com.gossip.arrienda_tu_finca.entities.Property;
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

    @Autowired
    public PropertyService(PropertyRepository propertyRepository, ModelMapper modelMapper, UserRepository userRepository, ImageRepository imageRepository) {
        this.propertyRepository = propertyRepository;
        this.modelMapper = modelMapper; 
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    // Crear propiedad
    public PropertyDTO createProperty(PropertyCreateDTO propertyCreateDTO) {
        Property property = modelMapper.map(propertyCreateDTO, Property.class); 
        property.setAvailable(true); 

        Long userId = userRepository.findIdByEmail(propertyCreateDTO.getOwnerEmail());

        if (userId == null) {
            throw new PropertyNotFoundException("User with email " + propertyCreateDTO.getOwnerEmail() + " not found when creating property");
        }

        property.setOwner(userRepository.findById(userId).get());

        Property savedProperty = propertyRepository.save(property);
        return modelMapper.map(savedProperty, PropertyDTO.class); 
    }

    public PropertyDTO getPropertyById(Long id) {
        Property property = propertyRepository.findById(id)
            .orElseThrow(() -> new PropertyNotFoundException("Property with ID " + id + " not found for fetching"));
        return modelMapper.map(property, PropertyDTO.class);
    }

    // Obtener todas las propiedades
    public List<PropertyDTO> getAllProperties() {
        List<Property> properties = propertyRepository.findAll();
        return properties.stream()
                .map(property -> modelMapper.map(property, PropertyDTO.class)) // Mapea cada entidad a DTO
                .collect(Collectors.toList());
    }

    // Actualizar propiedad
    public PropertyDTO updateProperty(Long id, PropertyUpdateDTO propertyUpdateDTO) {
        Property property = propertyRepository.findById(id)
            .orElseThrow(() -> new PropertyNotFoundException("To update property with ID " + id + " not found"));
        modelMapper.map(propertyUpdateDTO, property); 
        Property updatedProperty = propertyRepository.save(property);
        return modelMapper.map(updatedProperty, PropertyDTO.class);
    }


    // Desactivar propiedad (no eliminar)
    public void deactivateProperty(Long id) {
        Property property = propertyRepository.findById(id)
            .orElseThrow(() -> new PropertyNotFoundException("Property to deactivate with ID " + id + " not found"));
        property.setAvailable(false);
        propertyRepository.save(property);
    }
    

    // Subir foto (lógica de almacenamiento omitida)
    public void uploadPhoto(Long id, MultipartFile photo) throws IOException {
        Property property = propertyRepository.findById(id)
            .orElseThrow(() -> new PropertyNotFoundException("Property not found"));
        Image image = new Image();
        image.setName(photo.getOriginalFilename());
        image.setImageData(photo.getBytes());
        int imageId = imageRepository.save(image).getId();
        property.getImagesIds().add(imageId);
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
            throw new PropertyNotFoundException("Propiedades con el nombre " + name + " no fueron encontradas");
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

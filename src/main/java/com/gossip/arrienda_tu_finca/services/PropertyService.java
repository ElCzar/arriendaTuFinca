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
import com.gossip.arrienda_tu_finca.entities.Property;
import com.gossip.arrienda_tu_finca.exceptions.PropertyNotFoundException;
import com.gossip.arrienda_tu_finca.repositories.PropertyRepository;
import com.gossip.arrienda_tu_finca.repositories.UserRepository;
import java.util.Random;

@Service
public class PropertyService {
    private PropertyRepository propertyRepository;
    private ModelMapper modelMapper;
    private UserRepository userRepository;
    private Random random;

    @Autowired
    public PropertyService(PropertyRepository propertyRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.propertyRepository = propertyRepository;
        this.modelMapper = modelMapper; 
        this.userRepository = userRepository;
        this.random = new Random();  
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
        byte[] photoBytes;
        photoBytes = photo.getBytes();
        property.setPhoto(photoBytes);
    }
  
  public void setRandom(Random random) {
        this.random = random;
    }
    
    // Obtener las propiedades de un municipio aleatorio
    public List<PropertyListDTO> getPropertiesByRandomMunicipality() {
        List<String> municipalities = propertyRepository.findAllMunicipalities();
        String randomMunicipality = municipalities.get(random.nextInt(municipalities.size()));
        List<PropertyListDTO> properties = propertyRepository.findAllPropertyDTOByMunicipality(randomMunicipality);
        if (properties.isEmpty()) {
            throw new PropertyNotFoundException("Property by random municipality not found");
        }
        return properties;
    }

    // Obtener una propiedad por nombre
    public PropertyListDTO getPropertyByName(String name) {
        PropertyListDTO propertyDTO = propertyRepository.findPropertyDTOByName(name);
        if (propertyDTO == null) {
            throw new PropertyNotFoundException("Property by name not found");
        }
        return propertyDTO;
    }

    // Obtener una propiedad por municipio
    public PropertyListDTO getPropertyByMunicipality(String municipality) {
        PropertyListDTO propertyDTO = propertyRepository.findPropertyDTOByMunicipality(municipality);
        if (propertyDTO == null) {
            throw new PropertyNotFoundException("Property by municipality not found");
        }
        return propertyDTO;
    }

    // Obtener una propiedad por numero de personas
    public PropertyListDTO getPropertyByPeopleNumber(Integer peopleNumber) {
        PropertyListDTO propertyDTO = propertyRepository.findPropertyDTOByPeopleNumber(peopleNumber);
        if (propertyDTO == null) {
            throw new PropertyNotFoundException("Property by number of people not found");
        }
        return propertyDTO;
    }
}

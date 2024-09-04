package com.gossip.arrienda_tu_finca.services;

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

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private ModelMapper modelMapper;  

    // Crear propiedad
    public PropertyDTO createProperty(PropertyCreateDTO propertyCreateDTO) {
        Property property = modelMapper.map(propertyCreateDTO, Property.class); 
        property.setAvailable(true); 
        Property savedProperty = propertyRepository.save(property);
        return modelMapper.map(savedProperty, PropertyDTO.class); 
    }

    public PropertyDTO getPropertyById(Long id) {
        Property property = propertyRepository.findById(id)
            .orElseThrow(() -> new PropertyNotFoundException("Property with ID " + id + " not found"));
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
        .orElseThrow(() -> new PropertyNotFoundException("Property with ID " + id + " not found"));
    modelMapper.map(propertyUpdateDTO, property); 
    Property updatedProperty = propertyRepository.save(property);
    return modelMapper.map(updatedProperty, PropertyDTO.class);
}


    // Desactivar propiedad (no eliminar)
    public void deactivateProperty(Long id) {
        Property property = propertyRepository.findById(id)
            .orElseThrow(() -> new PropertyNotFoundException("Property with ID " + id + " not found"));
        property.setAvailable(false);  // Cambia el estado de la propiedad a "no disponible"
        propertyRepository.save(property);
    }
    

    // Subir foto (lógica de almacenamiento omitida)
    public void uploadPhoto(Long id, MultipartFile photo) {
        Property property = propertyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Property not found"));
        //aqui almaceno
    }
}

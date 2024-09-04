package com.gossip.arrienda_tu_finca.controllers;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gossip.arrienda_tu_finca.entities.Property;
import com.gossip.arrienda_tu_finca.repositories.PropertyRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TestPropertyController {

    @Autowired
    private MockMvc mvc;


    @Autowired
    private PropertyRepository propertyRepository;

    @BeforeEach
    void setUp() {
        propertyRepository.deleteAllInBatch(); // Limpiar la base de datos antes de cada prueba
    }


    @Test
    @Description("Prueba la creación de una propiedad válida")
    void testCreateProperty() throws Exception {
        // Crear JSON de PropertyCreateDTO
        String request = """
        {
            "name": "Finca La Esperanza",
            "description": "Hermosa finca en el campo",
            "municipality": "Medellin",
            "typeOfEntrance": "Carretera",
            "address": "Km 10 via a El Retiro",
            "pricePerNight": 150.0,
            "amountOfRooms": 3,
            "amountOfBathrooms": 2,
            "isPetFriendly": true,
            "hasPool": true,
            "hasGril": true,
            "ownerEmail": "owner@example.com"
        }
        """;

        mvc.perform(post("/property")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Finca La Esperanza"));
    }

    
    @Test
    @Description("Prueba obtener una propiedad por ID")
    void testGetPropertyById() throws Exception {
        // Crear y guardar una propiedad en la base de datos
        Property property = new Property();
        property.setName("Finca Bella");
        property.setDescription("Hermosa finca");
        property.setMunicipality("Bogota");
        propertyRepository.save(property);

        mvc.perform(get("/property/" + property.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Finca Bella"))
                .andExpect(jsonPath("$.description").value("Hermosa finca"));
    }

    @Test
    @Description("Prueba obtener todas las propiedades")
    void testGetAllProperties() throws Exception {
        // Crear y guardar una propiedad en la base de datos
        Property property = new Property();
        property.setName("Finca Bella");
        property.setDescription("Hermosa finca");
        property.setMunicipality("Bogota");
        propertyRepository.save(property);

        mvc.perform(get("/property")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Finca Bella"));
    }

   
    @Test
    @Description("Prueba la actualización de una propiedad existente")
    void testUpdateProperty() throws Exception {
        // Crear y guardar una propiedad en la base de datos
        Property property = new Property();
        property.setName("Finca Bella");
        property.setDescription("Hermosa finca");
        property.setMunicipality("Bogota");
        propertyRepository.save(property);

        // Crear JSON de PropertyUpdateDTO
        String updateRequest = """
        {
            "name": "Finca Bella Actualizada",
            "description": "Finca con piscina",
            "municipality": "Bogota",
            "pricePerNight": 250.0,
            "amountOfRooms": 5,
            "amountOfBathrooms": 3,
            "isPetFriendly": true,
            "hasPool": true,
            "hasGril": true
        }
        """;

        mvc.perform(put("/property/" + property.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Finca Bella Actualizada"));
    }

    
    @Test
    @Description("Prueba la desactivación de una propiedad existente")
    void testDeactivateProperty() throws Exception {
        // Crear y guardar una propiedad en la base de datos
        Property property = new Property();
        property.setName("Finca Bella");
        property.setDescription("Hermosa finca");
        property.setMunicipality("Bogota");
        property.setAvailable(true); // Inicialmente disponible
        propertyRepository.save(property);

        mvc.perform(delete("/property/" + property.getId()))
                .andExpect(status().isNoContent());

        // Verificar que la propiedad está desactivada
        Property deactivatedProperty = propertyRepository.findById(property.getId()).orElseThrow();
        assertFalse(deactivatedProperty.isAvailable()); // Verificar que isAvailable sea false
    }


    
    @Test
    @Description("Prueba la subida de una foto a una propiedad")
    void testUploadPhoto() throws Exception {
        // Crear y guardar una propiedad en la base de datos
        Property property = new Property();
        property.setName("Finca Bella");
        property.setDescription("Hermosa finca");
        property.setMunicipality("Bogota");
        propertyRepository.save(property);

        MockMultipartFile photoFile = new MockMultipartFile(
                "photo", "finca.jpg", "image/jpeg", "fake-image-content".getBytes());

        mvc.perform(multipart("/property/" + property.getId() + "/upload-photo")
                .file(photoFile))
                .andExpect(status().isNoContent());

        // No se puede verificar el contenido de la imagen directamente, pero se espera que la propiedad acepte el archivo
    }
}

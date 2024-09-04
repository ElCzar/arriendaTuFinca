package com.gossip.arrienda_tu_finca.controllers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.gossip.arrienda_tu_finca.ArriendaTuFincaApplication;
import com.gossip.arrienda_tu_finca.entities.Property;
import com.gossip.arrienda_tu_finca.repositories.PropertyRepository;

import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = ArriendaTuFincaApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(
    locations = "classpath:application-test.properties"
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class TestPropertyController {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PropertyRepository propertyRepository;



    @BeforeEach
    @Transactional
    void setUp() {
        propertyRepository.deleteAllInBatch();
    }


    @Test
    @DirtiesContext
    @Transactional
    @Description("Test the property creation where all the fields are valid")
    void givenCorrectProperty_whenCreateProperty_thenPropertyCreated() throws Exception {
        // Arrange
        String request = 
        """
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

        // Act
        mvc.perform(MockMvcRequestBuilders.post("/property")
            .contentType("application/json")
            .content(request))
            .andExpect(MockMvcResultMatchers.status().isOk());

        // Assert
        List<Property> properties = propertyRepository.findAll();
        assertEquals(1, properties.size());
        Property property = properties.get(0);
        assertEquals("Finca La Esperanza", property.getName());
        assertEquals("Hermosa finca en el campo", property.getDescription());
    }

  
    @Test
    @DirtiesContext
    @Transactional
    @Description("Test to get a property by ID")
    void givenProperty_whenGetPropertyById_thenReturnProperty() throws Exception {
        // Arrange
        Property property = new Property();
        property.setName("Finca Bella");
        property.setDescription("Hermosa finca");
        property.setMunicipality("Bogota");
        propertyRepository.save(property);
    
        // Act & Assert
        mvc.perform(MockMvcRequestBuilders.get("/property/" + property.getId())
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Finca Bella"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Hermosa finca"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.municipality").value("Bogota"));
    }
    

    @Test
    @DirtiesContext
    @Transactional
    @Description("Test to get all properties")
    void givenProperties_whenGetAllProperties_thenReturnPropertiesList() throws Exception {
        // Arrange
        Property property = new Property();
        property.setName("Finca Bella");
        property.setDescription("Hermosa finca");
        property.setMunicipality("Bogota");
        propertyRepository.save(property);

        // Act
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.get("/property")
            .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().isOk());

        // Assert
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        assertNotNull(contentAsString);
    }


    @Test
    @DirtiesContext
    @Transactional
    @Description("Test to update a property")
    void givenUpdatedProperty_whenUpdateProperty_thenPropertyUpdated() throws Exception {
        // Arrange
        Property property = new Property();
        property.setName("Finca Bella");
        property.setDescription("Hermosa finca");
        property.setMunicipality("Bogota");
        propertyRepository.save(property);

        String updateRequest = 
        """
        {
            "name": "Finca Actualizada",
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

        // Act
        mvc.perform(MockMvcRequestBuilders.put("/property/" + property.getId())
            .contentType("application/json")
            .content(updateRequest))
            .andExpect(MockMvcResultMatchers.status().isOk());

        // Assert
        Property updatedProperty = propertyRepository.findById(property.getId()).orElseThrow();
        assertEquals("Finca Actualizada", updatedProperty.getName());
    }

    
    @Test
    @DirtiesContext
    @Transactional
    @Description("Test to deactivate a property")
    void givenProperty_whenDeactivateProperty_thenPropertyDeactivated() throws Exception {
        // Arrange
        Property property = new Property();
        property.setName("Finca Bella");
        property.setDescription("Hermosa finca");
        property.setMunicipality("Bogota");
        property.setAvailable(true); // Initially available
        propertyRepository.save(property);

        // Act
        mvc.perform(MockMvcRequestBuilders.delete("/property/" + property.getId()))
            .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Assert
        Property deactivatedProperty = propertyRepository.findById(property.getId()).orElseThrow();
        assertFalse(deactivatedProperty.isAvailable());
    }


    @Test
    @DirtiesContext
    @Transactional
    @Description("Test to upload a photo to a property")
    void givenProperty_whenUploadPhoto_thenPhotoUploaded() throws Exception {
        // Arrange
        Property property = new Property();
        property.setName("Finca Bella");
        property.setDescription("Hermosa finca");
        propertyRepository.save(property);

        MockMultipartFile photoFile = new MockMultipartFile("photo", "finca.jpg", "image/jpeg", "fake-image-content".getBytes());

        // Act
        mvc.perform(MockMvcRequestBuilders.multipart("/property/" + property.getId() + "/upload-photo")
            .file(photoFile))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}

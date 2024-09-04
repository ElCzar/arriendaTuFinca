package com.gossip.arrienda_tu_finca.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
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
    @Transactional
    @Description("Test de creación de una propiedad cuando todos los campos son válidos")
    void givenCorrectProperty_whenCreateProperty_thenPropertyCreated() throws Exception {
        // JSON de solicitud
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

        // Realizar la solicitud
        mvc.perform(MockMvcRequestBuilders.post("/properties")
                .contentType("application/json")
                .content(request))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verificar que se creó la propiedad
        List<Property> properties = propertyRepository.findAll();
        assertEquals(1, properties.size());
        Property property = properties.get(0);
        assertNotNull(property);
        assertEquals("Finca La Esperanza", property.getName());
    }

    @ParameterizedTest
    @Transactional
    @MethodSource("provideInvalidPropertiesStream")
    @Description("Test de creación de una propiedad con diferentes datos inválidos")
    void givenInvalidProperty_whenCreateProperty_thenPropertyNotCreated(String jsonPayload) throws Exception {
        // Realizar la solicitud con datos inválidos
        mvc.perform(MockMvcRequestBuilders.post("/properties")
                .contentType("application/json")
                .content(jsonPayload))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verificar que no se creó ninguna propiedad
        List<Property> properties = propertyRepository.findAll();
        assertEquals(0, properties.size());
    }

    private static Stream<String> provideInvalidPropertiesStream() {
        return Stream.of(
            """
            {
                "name": null,
                "description": "Finca sin nombre",
                "municipality": "Medellin"
            }
            """,
            """
            {
                "name": "Finca",
                "description": null,
                "municipality": null
            }
            """
        );
    }

    // 3. Test de obtención de propiedad por ID
    @Test
    @Transactional
    @Description("Test de obtención de una propiedad por su ID")
    void givenPropertyExists_whenGetPropertyById_thenReturnProperty() throws Exception {
        // Insertar una propiedad en la base de datos
        Property property = new Property(null, "Finca Bella", "Hermosa finca", "Bogota", "Carretera", "Calle 123", true, 200.0, 4, 3, true, true, true, null);
        propertyRepository.save(property);

        // Realizar la solicitud
        mvc.perform(MockMvcRequestBuilders.get("/properties/" + property.getId())
                .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Finca Bella"));
    }

    
    @Test
    @Transactional
    @Description("Test de actualización de una propiedad existente")
    void givenPropertyExists_whenUpdateProperty_thenPropertyUpdated() throws Exception {
        // Insertar una propiedad en la base de datos
        Property property = new Property(null, "Finca Bella", "Hermosa finca", "Bogota", "Carretera", "Calle 123", true, 200.0, 4, 3, true, true, true, null);
        propertyRepository.save(property);

        // JSON de solicitud para actualizar
        String updateRequest = """
        {
            "name": "Finca Bella Actualizada",
            "description": "Finca con piscina",
            "municipality": "Bogota",
            "pricePerNight": 250.0,
            "amountOfRooms": 5,
            "amountOfBathrooms": 3
        }
        """;

        // Realizar la solicitud
        mvc.perform(MockMvcRequestBuilders.put("/properties/" + property.getId())
                .contentType("application/json")
                .content(updateRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Finca Bella Actualizada"));

        // Verificar la actualización
        Property updatedProperty = propertyRepository.findById(property.getId()).orElseThrow();
        assertEquals("Finca Bella Actualizada", updatedProperty.getName());
        assertEquals(250.0, updatedProperty.getPricePerNight());
    }

   
    @Test
    @Transactional
    @Description("Test para desactivar una propiedad existente")
    void givenPropertyExists_whenDeactivateProperty_thenPropertyUnavailable() throws Exception {
        // Insertar una propiedad en la base de datos
        Property property = new Property(null, "Finca Bella", "Hermosa finca", "Bogota", "Carretera", "Calle 123", true, 200.0, 4, 3, true, true, true, null);
        propertyRepository.save(property);

        // Realizar la solicitud para desactivar la propiedad
        mvc.perform(MockMvcRequestBuilders.delete("/properties/" + property.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verificar que la propiedad fue desactivada
        Property deactivatedProperty = propertyRepository.findById(property.getId()).orElseThrow();
        assertEquals(false, deactivatedProperty.isAvailable());
    }
}

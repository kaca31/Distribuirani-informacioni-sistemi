package com.example.catalogservice;

import com.example.catalogservice.dto.BookDto;
import com.example.catalogservice.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // koristi application-test.properties
class CatalogServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository repo;

    @Test
    void testCreateAndGetBook() throws Exception {
        BookDto dto = BookDto.builder()
                .title("Clean Architecture")
                .author("Robert C. Martin")
                .price(BigDecimal.valueOf(50))
                .stockQuantity(10)
                .build();

        // CREATE
        String content = mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        BookDto created = objectMapper.readValue(content, BookDto.class);

        // GET
        mockMvc.perform(get("/api/books/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Clean Architecture"));

        // GET ALL
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author").value("Robert C. Martin"));
    }
}

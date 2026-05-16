package com.roadmap.booktracker.controller;

import com.roadmap.booktracker.repo.AuthorRepository;
import com.roadmap.booktracker.test_util.testcontainers.AbstractPostgresIT;
import com.roadmap.booktracker.test_util.web.ApiVersioningResolver;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@DisplayName("Author Controller Integration Tests")
@AutoConfigureMockMvc
@Sql(scripts = {
        "classpath:db/migration/cleanup.sql",
        "classpath:db/migration/v2__test_data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:db/migration/cleanup.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthorControllerIT extends AbstractPostgresIT {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @PersistenceContext
    private EntityManager entityManager;

    @DisplayName("GET /api/{ver}/authors")
    @Nested
    class GetAllAuthors{
        private String endpoint = ApiVersioningResolver.resolve(AuthorController.class, "getAllAuthors", "/authors");

        @Test
        @DisplayName("Get all authors; 200")
        void getAllAuthors() throws Exception {
            mockMvc.perform(get(endpoint))
                    .andExpect(jsonPath("$", hasSize(8)))
                    .andExpect(status().isOk());
        }
    }

    @DisplayName("GET /api/{ver}/authors/{id}")
    @Nested
    class GetAuthorById {
        private String endpoint = ApiVersioningResolver.resolve(AuthorController.class, "getAuthorById", "/authors/{id}");
    // get author by id and check if author response fields are present 200
        @Test
        @DisplayName("Get author by valid id; 200")
        void getAuthorByValidId() throws Exception {
            mockMvc.perform(get(endpoint, "b1000000-0000-0000-0000-000000000001"))
                    .andExpect(jsonPath("$.id").value("b1000000-0000-0000-0000-000000000001"))
                    .andExpect(jsonPath("$.firstName").value("George"))
                    .andExpect(jsonPath("$.lastName").value("Orwell"))
                    .andExpect(jsonPath("$.books", hasSize(2)))
                    .andExpect(status().isOk());
        }
        // get author by id with invalid id format 400
        @Test
        @DisplayName("Get author by invalid id format; 400")
        void getAuthorByInvalidId() throws Exception {
            mockMvc.perform(get(endpoint, "invalid-id"))
                    .andExpect(status().isBadRequest());
        }
        // get author by id with nonexistent id 404
        @Test
        @DisplayName("Get author by nonexistent id; 404")
        void getAuthorByNonexistentId() throws Exception {
            mockMvc.perform(get(endpoint, "a1000000-0000-0000-0000-000000000010"))
                    .andExpect(status().isNotFound());
        }
    }
}

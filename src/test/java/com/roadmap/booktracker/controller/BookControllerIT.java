package com.roadmap.booktracker.controller;

import com.roadmap.booktracker.dto.book.CreateBookRequest;
import com.roadmap.booktracker.entity.Author;
import com.roadmap.booktracker.entity.Book;
import com.roadmap.booktracker.entity.Genre;
import com.roadmap.booktracker.repo.AuthorRepository;
import com.roadmap.booktracker.repo.BookRepository;
import com.roadmap.booktracker.repo.GenreRepository;
import com.roadmap.booktracker.test_util.testcontainers.AbstractPostgresIT;
import com.roadmap.booktracker.test_util.web.ApiVersioningResolver;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Book Controller Integration Tests")
@AutoConfigureMockMvc
@Sql(scripts = {
//        "classpath:db/migration/cleanup.sql",
        "classpath:db/migration/v2__test_data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(scripts = "classpath:db/migration/cleanup.sql",
//        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookControllerIT extends AbstractPostgresIT {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private BookController bookController;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager.clear();
    }

    @DisplayName("POST /api/{ver}/books")
    @Nested
    class CreateBook{
        private String endpoint = ApiVersioningResolver.resolve(BookController.class, "createBook", "/books");

        @Transactional
        @Test
        @DisplayName("Create book with valid data; 201")
        void createBookWithValidData() throws Exception {
            List<UUID> genres = List.of(UUID.fromString("a1000000-0000-0000-0000-000000000002"));
            List<UUID> authors = List.of(UUID.fromString("b1000000-0000-0000-0000-000000000003"), UUID.fromString("b1000000-0000-0000-0000-000000000006"));

            CreateBookRequest request = new CreateBookRequest(
                    "test book",
                    "",
                    authors,
                    100,
                    2000,
                    genres);

            mockMvc
                    .perform(
                            post(endpoint)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isCreated())
                    .andReturn();

            assertTrue(bookRepository.existsByTitle(request.title()));

            Book savedBook = bookRepository.findByTitle(request.title()).orElseThrow();
            assertThat(savedBook.getAuthors())
                    .extracting(Author::getId)
                    .contains(authors.get(0));
            assertThat(savedBook.getGenres())
                    .extracting(Genre::getId)
                    .contains(genres.get(0));
        }

        @Transactional
        @Test
        @DisplayName("Create book with invalid data; 400")
        void createBookWithInvalidData() throws Exception {
            CreateBookRequest request = new CreateBookRequest(
                    "",
                    "",
                    List.of(UUID.randomUUID()),
                    100,
                    2000,
                    List.of(UUID.randomUUID())
            );

            mockMvc
                    .perform(
                            post(endpoint)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isBadRequest());
        }

        // create book with nonexistent author ; 400
        @Transactional
        @Test
        @DisplayName("Create book with nonexistent author; 404")
        void createBookWithNonexistentAuthor() throws Exception {
            List<UUID> genres = List.of(UUID.fromString("a1000000-0000-0000-0000-000000000002"));
            List<UUID> authors = List.of(UUID.randomUUID());

            CreateBookRequest request = new CreateBookRequest(
                    "test book",
                    "",
                    authors,
                    100,
                    2000,
                    genres
            );

            mockMvc
                    .perform(
                            post(endpoint)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isNotFound());
        }
    }
}

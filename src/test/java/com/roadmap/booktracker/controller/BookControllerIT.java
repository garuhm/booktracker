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
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Book Controller Integration Tests")
@AutoConfigureMockMvc
@Sql(scripts = {
        "classpath:db/migration/cleanup.sql",
        "classpath:db/migration/v2__test_data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:db/migration/cleanup.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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

    @DisplayName("GET /api/{ver}/books")
    @Nested
    class GetAllBooks{
        private String endpoint = ApiVersioningResolver.resolve(BookController.class, "getAllBooks", "/books");

        @Test
        @DisplayName("Get all books with no filter; 200")
        void getAllBooksWithNoFilter() throws Exception {
            mockMvc
                    .perform(get(endpoint))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(15)))
                    .andExpect(jsonPath("$.totalElements").value(15))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Get all books filtering by title; 200")
        void getAllBooksFilteringByTitle() throws Exception {
            mockMvc
                    .perform(get(endpoint + "?title=dune"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.totalElements").value(2))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(status().isOk());

            mockMvc
                    .perform(get(endpoint + "?title=the"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(8)))
                    .andExpect(jsonPath("$.totalElements").value(8))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(status().isOk());

            mockMvc
                    .perform(get(endpoint + "?title=harry potter"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(0)))
                    .andExpect(jsonPath("$.totalElements").value(0))
                    .andExpect(jsonPath("$.totalPages").value(0))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Get all books filtering by min rating; 200")
        void getAllBooksFilteringByMinRating() throws Exception {
            mockMvc
                    .perform(get(endpoint + "?minRating=4.6"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(6)))
                    .andExpect(jsonPath("$.totalElements").value(6))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(status().isOk());

            mockMvc
                    .perform(get(endpoint + "?minRating=4.8"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.totalElements").value(2))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(status().isOk());

            mockMvc
                    .perform(get(endpoint + "?minRating=5.0"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(0)))
                    .andExpect(jsonPath("$.totalElements").value(0))
                    .andExpect(jsonPath("$.totalPages").value(0))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Get all books filtering by exact publishing year; 200")
        void getAllBooksFilteringByExactPublishingYear() throws Exception {
            mockMvc
                    .perform(get(endpoint + "?publishedYear=1969"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.totalElements").value(2))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(status().isOk());

            mockMvc
                    .perform(get(endpoint + "?publishedYear=1949"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(1)))
                    .andExpect(jsonPath("$.totalElements").value(1))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(status().isOk());

            mockMvc
                    .perform(get(endpoint + "?publishedYear=2000"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(0)))
                    .andExpect(jsonPath("$.totalElements").value(0))
                    .andExpect(jsonPath("$.totalPages").value(0))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Get all books filtering by publishing year range; 200")
        void getAllBooksFilteringByPublishingYearRange() throws Exception {
            mockMvc
                    .perform(get(endpoint + "?publishedYearTo=1945"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(4)))
                    .andExpect(jsonPath("$.totalElements").value(4))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(status().isOk());

            mockMvc
                    .perform(get(endpoint + "?publishedYearFrom=1977"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(4)))
                    .andExpect(jsonPath("$.totalElements").value(4))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(status().isOk());

            mockMvc
                    .perform(get(endpoint + "?publishedYearFrom=1950&publishedYearTo=1974"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(6)))
                    .andExpect(jsonPath("$.totalElements").value(6))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Get all books filtering by author id; 200")
        void getAllBooksFilteringByAuthorId() throws Exception {
            mockMvc
                    .perform(get(endpoint + "?authorId=b1000000-0000-0000-0000-000000000001"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.totalElements").value(2))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(status().isOk());

            mockMvc
                    .perform(get(endpoint + "?authorId=b1000000-0000-0000-0000-000000000002 "))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.totalElements").value(2))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(status().isOk());

            mockMvc
                    .perform(get(endpoint + "?authorId=b1000000-0000-0000-0000-000000000007"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.totalElements").value(2))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Get all books filtering by genre id; 200")
        void getAllBooksFilteringByGenreId() throws Exception {
            mockMvc
                    .perform(get(endpoint + "?genreId=a1000000-0000-0000-0000-000000000001"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(5)))
                    .andExpect(jsonPath("$.totalElements").value(5))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(status().isOk());

            mockMvc
                    .perform(get(endpoint + "?genreId=a1000000-0000-0000-0000-000000000004"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.totalElements").value(2))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(status().isOk());

            mockMvc
                    .perform(get(endpoint + "?genreId=a1000000-0000-0000-0000-000000000006"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.totalElements").value(2))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Get all books with multiple filters; 200")
        void getAllBooksWithMultipleFilters() throws Exception {
            mockMvc
                    .perform(get(endpoint + "?genreId=a1000000-0000-0000-0000-000000000003&authorId=b1000000-0000-0000-0000-000000000002"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.totalElements").value(2))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(status().isOk());

            mockMvc
                    .perform(get(endpoint + "?title=1984&minRating=4.5&publishedYear=1949"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(1)))
                    .andExpect(jsonPath("$.totalElements").value(1))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(status().isOk());

            mockMvc
                    .perform(get(endpoint + "?authorId=b1000000-0000-0000-0000-000000000001&genreId=a1000000-0000-0000-0000-000000000004"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(0)))
                    .andExpect(jsonPath("$.totalElements").value(0))
                    .andExpect(jsonPath("$.totalPages").value(0))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Get all books with sorting and limits; 200")
        void getAllBooksWithSortingAndLimits() throws Exception {
            mockMvc
                    .perform(get(endpoint + "?sort=averageRating,desc&size=2"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.content[0].title").value("The Lord of the Rings"))
                    .andExpect(jsonPath("$.content[1].title").value("The Hobbit"))
                    .andExpect(jsonPath("$.totalElements").value(15))
                    .andExpect(jsonPath("$.totalPages").value(8))
                    .andExpect(status().isOk());

            mockMvc
                    .perform(get(endpoint + "?sort=publishedYear,asc&size=1"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(1)))
                    .andExpect(jsonPath("$.content[0].title").value("Murder on the Orient Express"))
                    .andExpect(jsonPath("$.totalElements").value(15))
                    .andExpect(jsonPath("$.totalPages").value(15))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Get all books with pagination; 200")
        void getAllBooksWithPagination() throws Exception{
            mockMvc
                    .perform(get(endpoint + "?page=0&size=5"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(5)))
                    .andExpect(jsonPath("$.totalElements").value(15))
                    .andExpect(jsonPath("$.totalPages").value(3))
                    .andExpect(status().isOk());

            mockMvc
                    .perform(get(endpoint + "?page=1&size=5"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(5)))
                    .andExpect(jsonPath("$.totalElements").value(15))
                    .andExpect(jsonPath("$.totalPages").value(3))
                    .andExpect(status().isOk());

            mockMvc
                    .perform(get(endpoint + "?page=2&size=5"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(5)))
                    .andExpect(jsonPath("$.totalElements").value(15))
                    .andExpect(jsonPath("$.totalPages").value(3))
                    .andExpect(status().isOk());

            mockMvc
                    .perform(get(endpoint + "?page=3&size=5"))
                    .andExpect(jsonPath("$.content").exists())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content", hasSize(0)))
                    .andExpect(jsonPath("$.totalElements").value(15))
                    .andExpect(jsonPath("$.totalPages").value(3))
                    .andExpect(status().isOk());
        }
    }
}

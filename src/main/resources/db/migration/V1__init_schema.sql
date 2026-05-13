CREATE TABLE books (
                       id             UUID          NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
                       title          VARCHAR(255)  NOT NULL,
                       description    TEXT,
                       pages          INTEGER       NOT NULL,
                       published_year INTEGER       NOT NULL,
                       average_rating FLOAT         NOT NULL DEFAULT 0.0,
                       created_at     TIMESTAMP     NOT NULL,
                       updated_at     TIMESTAMP     NOT NULL
);

CREATE TABLE authors (
                         id         UUID         NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
                         first_name VARCHAR(255) NOT NULL,
                         last_name  VARCHAR(255) NOT NULL,
                         created_at TIMESTAMP    NOT NULL,
                         updated_at TIMESTAMP    NOT NULL
);

CREATE TABLE genres (
                        id         UUID         NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
                        name       VARCHAR(100) NOT NULL,
                        created_at TIMESTAMP    NOT NULL,
                        updated_at TIMESTAMP    NOT NULL
);

CREATE TABLE book_authors (
                              author_id UUID NOT NULL REFERENCES authors(id) ON DELETE CASCADE,
                              book_id   UUID NOT NULL REFERENCES books(id)   ON DELETE CASCADE,
                              PRIMARY KEY (author_id, book_id)
);

CREATE TABLE book_genres (
                             genre_id UUID NOT NULL REFERENCES genres(id) ON DELETE CASCADE,
                             book_id  UUID NOT NULL REFERENCES books(id)  ON DELETE CASCADE,
                             PRIMARY KEY (genre_id, book_id)
);

CREATE TABLE reviews (
                         id         UUID      NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
                         book_id    UUID      NOT NULL REFERENCES books(id) ON DELETE CASCADE,
                         rating     INTEGER   NOT NULL CHECK (rating >= 1 AND rating <= 5),
                         review     TEXT,
                         created_at TIMESTAMP NOT NULL,
                         updated_at TIMESTAMP NOT NULL
);

CREATE TABLE reading_list_entries (
                                      id          UUID         NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
                                      book_id     UUID         NOT NULL UNIQUE REFERENCES books(id) ON DELETE CASCADE,
                                      status      VARCHAR(20),
                                      pages_read  INTEGER      NOT NULL DEFAULT 0,
                                      started_at  TIMESTAMP,
                                      finished_at TIMESTAMP,
                                      created_at  TIMESTAMP    NOT NULL,
                                      updated_at  TIMESTAMP    NOT NULL
);
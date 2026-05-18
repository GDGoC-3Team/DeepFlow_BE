CREATE TABLE IF NOT EXISTS books (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NULL,
    author VARCHAR(255) NULL,
    cover_image_url VARCHAR(255) NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS book_pages (
    id BIGINT NOT NULL AUTO_INCREMENT,
    book_id BIGINT NOT NULL,
    content LONGTEXT NULL,
    character_count INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_book_pages_book
        FOREIGN KEY (book_id)
        REFERENCES books(id)
);

CREATE TABLE IF NOT EXISTS sentences (
    id BIGINT NOT NULL AUTO_INCREMENT,
    content TEXT NOT NULL,
    image_url VARCHAR(255) NULL,
    book_title VARCHAR(255) NULL,
    author VARCHAR(255) NULL,
    created_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
);

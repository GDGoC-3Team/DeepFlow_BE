CREATE TABLE IF NOT EXISTS books (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NULL,
    author VARCHAR(255) NULL,
    cover_image_url VARCHAR(255) NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS book_imports (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NULL,
    author VARCHAR(255) NULL,
    content LONGTEXT NULL,
    sentence_feed LONGTEXT NULL,
    character_count INT NOT NULL,
    PRIMARY KEY (id)
);

LOAD DATA INFILE '/var/lib/mysql-files/data.csv'
INTO TABLE book_imports
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(title, author, content, sentence_feed, character_count);

INSERT INTO books (id, title, author, cover_image_url)
SELECT id, title, author, NULL
FROM book_imports;

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

INSERT INTO book_pages (book_id, content, character_count)
SELECT id, content, character_count
FROM book_imports;

DROP TABLE book_imports;

CREATE TABLE IF NOT EXISTS book_pages (
    id BIGINT NOT NULL AUTO_INCREMENT,
    book_id BIGINT NULL,
    page_number INT NOT NULL,
    content LONGTEXT NULL,
    character_count INT NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_book_pages_book
        FOREIGN KEY (book_id)
        REFERENCES books(id)
);


LOAD DATA INFILE '/docker-entrypoint-initdb.d/data.csv'
INTO TABLE book_pages
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(book_id, page_number, content, character_count);
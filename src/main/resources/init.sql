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

CREATE TABLE IF NOT EXISTS sentences (
    id BIGINT NOT NULL AUTO_INCREMENT,
    content TEXT NOT NULL,
    image_url VARCHAR(255) NULL,
    book_title VARCHAR(255) NULL,
    author VARCHAR(255) NULL,
    created_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO sentences (content, image_url, book_title, author, created_at)
WITH RECURSIVE sequence AS (
    SELECT 1 AS n
    UNION ALL
    SELECT n + 1
    FROM sequence
    WHERE n < 1000
)
SELECT
    TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(REPLACE(bi.sentence_feed, '\r', ''), '\n', sequence.n), '\n', -1)) AS content,
    CONCAT(
        'https://storage.googleapis.com/deepflow-image-storage/background-image/image_',
        FLOOR(1 + RAND() * 8),
        '.png'
    ) AS image_url,
    bi.title,
    bi.author,
    NOW(6)
FROM book_imports bi
JOIN sequence
    ON sequence.n <= 1 + LENGTH(REPLACE(bi.sentence_feed, '\r', ''))
        - LENGTH(REPLACE(REPLACE(bi.sentence_feed, '\r', ''), '\n', ''))
WHERE TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(REPLACE(bi.sentence_feed, '\r', ''), '\n', sequence.n), '\n', -1)) <> '';

DROP TABLE book_imports;

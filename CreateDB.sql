CREATE TABLE news (
    id SERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    url TEXT NOT NULL,
    source TEXT,
    published_date DATE
);

CREATE INDEX news_title_fulltext_idx ON news USING gin(to_tsvector('english', title));

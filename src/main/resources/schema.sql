DROP TABLE IF EXISTS users, items, bookings, comments CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    owner BIGINT NOT NULL REFERENCES users(id),
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    available BOOLEAN
);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date TIMESTAMP WITHOUT TIME ZONE,
    item_id BIGINT NOT NULL REFERENCES items(id),
    booker_id BIGINT NOT NULL REFERENCES users(id),
    status VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text VARCHAR(255),
    item_id BIGINT NOT NULL REFERENCES items(id),
    author_id BIGINT NOT NULL REFERENCES users(id),
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
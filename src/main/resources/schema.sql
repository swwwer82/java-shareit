DROP TABLE IF EXISTS comments, requests, bookings, users, items;

CREATE TABLE IF NOT EXISTS users (
                                     id            BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                     name          VARCHAR(255) NOT NULL,
                                     email         VARCHAR(255) UNIQUE NOT NULL,
                                     created_at    TIMESTAMP NOT NULL DEFAULT(CURRENT_TIMESTAMP())
);

CREATE TABLE IF NOT EXISTS items (
                                     id            BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                     name          VARCHAR(255) NOT NULL,
                                     description   VARCHAR(255) NOT NULL,
                                     available     BOOLEAN NOT NULL DEFAULT(false),
                                     owner_id      BIGINT NOT NULL,
                                     created_at    TIMESTAMP NOT NULL DEFAULT(CURRENT_TIMESTAMP())
);
ALTER TABLE items ADD FOREIGN KEY (owner_id) REFERENCES users (id);

CREATE TABLE IF NOT EXISTS bookings (
                                        id            BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                        start_date    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                        end_date      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                        item_id       BIGINT NOT NULL,
                                        booker_id     BIGINT NOT NULL,
                                        status        VARCHAR(60) NOT NULL,
                                        created_at    TIMESTAMP NOT NULL DEFAULT(CURRENT_TIMESTAMP())
);
ALTER TABLE bookings ADD FOREIGN KEY (item_id) REFERENCES items (id);
ALTER TABLE bookings ADD FOREIGN KEY (booker_id) REFERENCES users (id);

CREATE TABLE IF NOT EXISTS requests (
                                        id            BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                        description   VARCHAR(255) NOT NULL,
                                        requestor_id  BIGINT NOT NULL,
                                        created_at    TIMESTAMP NOT NULL DEFAULT(CURRENT_TIMESTAMP())
);
ALTER TABLE requests ADD FOREIGN KEY (requestor_id) REFERENCES users (id);

CREATE TABLE IF NOT EXISTS comments (
                                        id            BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                        text          VARCHAR(255) NOT NULL,
                                        item_id       BIGINT NOT NULL,
                                        author_id     BIGINT NOT NULL,
                                        add_date      TIMESTAMP NOT NULL,
                                        created_at    TIMESTAMP NOT NULL DEFAULT(CURRENT_TIMESTAMP())
);
ALTER TABLE comments ADD FOREIGN KEY (item_id) REFERENCES items (id);
ALTER TABLE comments ADD FOREIGN KEY (author_id) REFERENCES users (id);
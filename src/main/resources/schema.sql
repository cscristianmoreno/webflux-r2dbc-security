CREATE TABLE IF NOT EXISTS users (
    id serial NOT NULL PRIMARY KEY,
    username varchar(32) NOT NULL UNIQUE,
    password varchar(256) NOT NULL,
    authority text ARRAY NOT NULL
);
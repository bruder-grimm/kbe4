DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS songs CASCADE;
DROP TABLE IF EXISTS playlists CASCADE;
DROP TABLE IF EXISTS song_playlist CASCADE;

CREATE TABLE users(
    userId VARCHAR(50) NOT NULL PRIMARY KEY,
    key VARCHAR(50) NOT NULL ,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL
);

CREATE TABLE songs(
    id SERIAL PRIMARY KEY NOT NULL,
    title VARCHAR(50) NOT NULL,
    artist VARCHAR(50) NOT NULL,
    label VARCHAR(50) NOT NULL,
    released INTEGER NOT NULL
);

CREATE TABLE playlists(
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(50) NOT NULL,
    owner_id VARCHAR(50) REFERENCES users (userId),
    visible BOOLEAN
);

CREATE TABLE song_playlist(
    playlist_id INTEGER REFERENCES playlists (id),
    song_id INTEGER REFERENCES songs (id)
);
INSERT INTO users(userId, key, firstname, lastName) VALUES ('mmuster', 'ENCRYPTEDKEY1', 'Maxime', 'Muster');
INSERT INTO users(userId, key, firstname, lastName) VALUES ('eschuler', 'ENCRYPTEDKEY2', 'Elena', 'Schuler');


INSERT INTO songs (title, artist, label, released) VALUES ('MacArthur Park', 'Richard Harris', 'Dunhill Records', 1968);
INSERT INTO songs (title, artist, label, released) VALUES ('Afternoon Delight', 'Starland Vocal Band', 'Windsong', 1976);
INSERT INTO songs (title, artist, label, released) VALUES ('Muskrat Love', 'Captain and Tennille', 'A&M', 1976);
INSERT INTO songs (title, artist, label, released) VALUES ('Sussudio', 'Phil Collins', 'Virgin', 1985);
INSERT INTO songs (title, artist, label, released) VALUES ('We Built This City', 'Starship', 'Grunt/RCA', 1985);
INSERT INTO songs (title, artist, label, released) VALUES ('Achy Breaky Heart', 'Billy Ray Cyrus', 'PolyGram Mercury', 1992);
INSERT INTO songs (title, artist, label, released) VALUES ('What’s Up?', '4 Non Blondes', 'Interscope', 1993);
INSERT INTO songs (title, artist, label, released) VALUES ('Who Let the Dogs Out?', 'Baha Men', 'S-Curve', 2000);
INSERT INTO songs (title, artist, label, released) VALUES ('My Humps', 'Black Eyed Peas', 'Universal Music', 2005);
INSERT INTO songs (title, artist, label, released) VALUES ('Chinese Food', 'Alison Gold', 'PMW Live', 2013);

INSERT INTO playlists (name, owner_id, visible) VALUES ('a', 'mmuster', true);
INSERT INTO playlists (name, owner_id, visible) VALUES ('b', 'mmuster', false);
INSERT INTO playlists (name, owner_id, visible) VALUES ('c', 'eschuler', true);
INSERT INTO playlists (name, owner_id, visible) VALUES ('d', 'eschuler', false);

INSERT INTO song_playlist (playlist_id, song_id) VALUES (1, 1);
INSERT INTO song_playlist (playlist_id, song_id) VALUES (1, 3);

INSERT INTO song_playlist (playlist_id, song_id) VALUES (2, 1);
INSERT INTO song_playlist (playlist_id, song_id) VALUES (2, 2);

INSERT INTO song_playlist (playlist_id, song_id) VALUES (2, 3);
INSERT INTO song_playlist (playlist_id, song_id) VALUES (2, 4);

INSERT INTO song_playlist (playlist_id, song_id) VALUES (3, 4);
INSERT INTO song_playlist (playlist_id, song_id) VALUES (3, 5);

INSERT INTO song_playlist (playlist_id, song_id) VALUES (4, 3);
INSERT INTO song_playlist (playlist_id, song_id) VALUES (4, 6);

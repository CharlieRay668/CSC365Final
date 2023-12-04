CREATE TABLE Genre (
	name varchar(80) PRIMARY KEY
);

CREATE TABLE Artist (
	aid varchar(30) PRIMARY KEY,
    name varchar(200),
    popularity INTEGER,
    uri varchar(200),
    followers INTEGER
	);

CREATE TABLE Track (
	tid varchar(30) PRIMARY KEY,
	duration INTEGER,
    explicit BOOLEAN,
    name varchar(200),
    uri varchar(200)
    );

CREATE TABLE Playlist (
    pid INTEGER PRIMARY KEY AUTO_INCREMENT,
    name varchar(200)
    );
    
CREATE TABLE Album (
	albumID varchar(30) PRIMARY KEY,
	type ENUM("album", "single", "compilation"),
    totalTracks INTEGER,
    name varchar(200),
    releaseDate DATE,
    uri varchar(200)
    );
	
CREATE TABLE ArtistGenres (
	aid varchar(30),
    genre varchar(80),
    PRIMARY KEY (aid, genre),
    FOREIGN KEY (aid) REFERENCES Artist(aid),
    FOREIGN KEY (genre) REFERENCES Genre(name)
    );
    
CREATE TABLE Performances (
	tid varchar(30),
    aid varchar(30),
    PRIMARY KEY (tid, aid),
    FOREIGN KEY (tid) REFERENCES Track(tid),
    FOREIGN KEY (aid) REFERENCES Artist(aid)
    );
    
CREATE TABLE ArtistAlbums (
	aid varchar(30),
    albumID varchar(30),
    PRIMARY KEY (aid, albumID),
    FOREIGN KEY (aid) REFERENCES Artist(aid),
    FOREIGN KEY (albumID) REFERENCES Album(albumID)
    );
    
CREATE TABLE PlaylistTracks (
    pid INTEGER,
    tid varchar(30),
    PRIMARY KEY (pid, tid),
    FOREIGN KEY (pid) REFERENCES Playlist(pid),
    FOREIGN KEY (tid) REFERENCES Track(tid)
    );
    
CREATE TABLE AlbumTracks (
	tid varchar(30),
    albumID varchar(30),
    PRIMARY KEY (tid, albumID),
    FOREIGN KEY (tid) REFERENCES Track(tid),
    FOREIGN KEY (albumID) REFERENCES Album(albumID)
    );

CREATE TABLE Plays (
	playID INTEGER PRIMARY KEY AUTO_INCREMENT, 
	tid varchar(30),
    datePlayed DATE
    );
    

DROP TABLE Album;
DROP TABLE Track;
DROP TABLE Artist;
DROP TABLE Playlist;
DROP TABLE Genre;
DROP TABLE Plays;
DROP TABLE ArtistGenres;
DROP TABLE Performances;
DROP TABLE ArtistAlbums;
DROP TABLE PlaylistTracks;
DROP TABLE AlbumTracks;

SELECT * FROM Album;
SELECT * FROM Track;
SELECT * FROM Artist;
SELECT * FROM Playlist;
SELECT * FROM Genre;
SELECT * FROM Plays;
SELECT * FROM ArtistGenres;
SELECT * FROM Performances;
SELECT * FROM ArtistAlbums;
SELECT * FROM PlaylistTracks;
SELECT * FROM AlbumTracks;

INSERT INTO Playlist (name) VALUES ("Liked Songs");
    
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
    pid varchar(30) PRIMARY KEY,
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
    pid varchar(30),
    tid varchar(30),
    PRIMARY KEY (pid, tid),
    FOREIGN KEY (pid) REFERENCES Playlist(pid),
    FOREIGN KEY (tid) REFERENCES Track(tid)
    );

DROP TABLE Album;
DROP TABLE Track;
DROP TABLE Artist;
DROP TABLE Playlist;
DROP TABLE Genre;
DROP TABLE ArtistGenres;
DROP TABLE Performances;
DROP TABLE ArtistAlbums;
DROP TABLE PlaylistTracks;

    
    
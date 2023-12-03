import com.mysql.fabric.xmlrpc.base.Array;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

class SQLConnector {
    static private Connection connect;
    static private APIPuller api;
    public SQLConnector() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connect = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306?user=chray&password=talltree");
            this.api = new APIPuller();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertGenre(String genre) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeUpdate("INSERT INTO chray.Genre (name) VALUES ('"+genre+"');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void linkArtistGenre(String artistID, String genre) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeUpdate("INSERT INTO chray.ArtistGenres (aid, genre) VALUES ('"+artistID+"','"+genre+"');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void linkArtistAlbum(String artistID, String albumID) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeUpdate("INSERT INTO chray.ArtistAlbums (aid, albumID) VALUES ('"+artistID+"','"+albumID+"');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void linkPerformance(String artistID, String trackID) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeUpdate("INSERT INTO chray.Performances (aid, tid) VALUES ('"+artistID+"','"+trackID+"');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void linkAlbumTrack(String albumID, String trackID) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeUpdate("INSERT INTO chray.AlbumTracks (albumID, tid) VALUES ('"+albumID+"','"+trackID+"');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertArtist(String artist, String artistID, int popularity, String uri, int followers) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeUpdate("INSERT INTO chray.Artist (aid, name, popularity, uri, followers) VALUES ('"+artistID+"','"+artist+"',"+popularity+",'"+uri+"',"+followers+");");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void instertTrack(String tid, int duration, boolean explicit, String name, String uri) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeUpdate("INSERT INTO chray.Track (tid, duration, explicit, name, uri) VALUES ('"+tid+"',"+duration+","+explicit+",'"+name+"','"+uri+"');");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void insertAlbum(String albumID, String type, int totalTracks, String name, String releaseDate, String uri) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeUpdate("INSERT INTO chray.Album (albumID, type, totalTracks, name, releaseDate, uri) VALUES ('"+albumID+"','"+type+"',"+totalTracks+",'"+name+"','"+releaseDate+"','"+uri+"');");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void insertPlaylist(String pid, String name) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeUpdate("INSERT INTO chray.Playlist (pid, name) VALUES ('"+pid+"','"+name+"');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    This method operates under the assumption that the track is not already in the database,
//    That just means that it also hits spotify's API for the rest of the information
    public void addUnseenTrack(Map<String, Object> trackInfo) throws IOException, InterruptedException {
        Map<String, Object> track = (Map<String, Object>) trackInfo.get("track");
        String tid = (String) track.get("id");
        int duration = (int) track.get("duration_ms");
        boolean explicit = (boolean) track.get("explicit");
        String name = (String) track.get("name");
        String uri = (String) track.get("uri");
        this.instertTrack(tid, duration, explicit, name, uri);

        Map<String, Object> album = (Map<String, Object>) trackInfo.get("album");
        String albumID = (String) album.get("id");
        String type = (String) album.get("type");
        int totalTracks = (int) album.get("total_tracks");
        String albumName = (String) album.get("name");
        String releaseDate = (String) album.get("release_date");
        String albumURI = (String) album.get("uri");
        this.insertAlbum(albumID, type, totalTracks, albumName, releaseDate, albumURI);
        this.linkAlbumTrack(albumID, tid);
        ArrayList<Map<String, Object>> artists = (ArrayList<Map<String, Object>>) trackInfo.get("artists");
        for (Map<String, Object> artist : artists) {
            String artistID = (String) artist.get("id");
            String artistName = (String) artist.get("name");
            Object popularity = artist.get("popularity");
            if (popularity == null) {
                popularity = 0;
            } else {
                popularity = (int) popularity;
            }
            String artistURI = (String) artist.get("uri");
            int followers = (int) artist.get("followers");
            ArrayList<String> genres = (ArrayList<String>) artist.get("genres");
            this.insertArtist(artistName, artistID, (int) popularity, artistURI, followers);
            if (genres != null) {
                for (String genre : genres) {
                    this.insertGenre(genre);
                    this.linkArtistGenre(artistID, genre);
                }
            }
            this.linkArtistAlbum(artistID, albumID);
            this.linkPerformance(artistID, tid);
        }
    }

    public void addTrackToPlaylist(String pid, String tid) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeUpdate("SELECT * FROM chray.Track WHERE tid = '"+tid+"';");
            ResultSet rs = stmt.getResultSet();
            if (rs.next()) {
                stmt.executeUpdate("INSERT INTO chray.PlaylistTracks (pid, tid) VALUES ('"+pid+"','"+tid+"');");
            } else {
                Map<String, Object> trackInfo = this.api.getTrack(tid);
                this.addUnseenTrack(trackInfo);
                stmt.executeUpdate("INSERT INTO chray.PlaylistTracks (pid, tid) VALUES ('"+pid+"','"+tid+"');");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createPlaylist(String name) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeUpdate("INSERT INTO chray.Playlist (name) VALUES ('"+name+"');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearTables() {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeUpdate("DELETE FROM chray.PlaylistTracks;");
            stmt.executeUpdate("DELETE FROM chray.ArtistAlbums;");
            stmt.executeUpdate("DELETE FROM chray.Performances;");
            stmt.executeUpdate("DELETE FROM chray.ArtistGenres;");
            stmt.executeUpdate("DELETE FROM chray.AlbumTracks;");
            stmt.executeUpdate("DELETE FROM chray.Plays;");
            stmt.executeUpdate("DELETE FROM chray.Track;");
            stmt.executeUpdate("DELETE FROM chray.Album;");
            stmt.executeUpdate("DELETE FROM chray.Artist;");
            stmt.executeUpdate("DELETE FROM chray.Playlist;");
            stmt.executeUpdate("DELETE FROM chray.Genre;");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        APIPuller api = new APIPuller();
        SQLConnector sql = new SQLConnector();
        try {
            sql.clearTables();
//            Query string
            String track = "Just Wanna Rock";
//            Hit the backend
            Map<String, ArrayList<Map<String, Object>>> tracks = api.querySpotify(track);
//            Display the tracks
            ArrayList<Map<String, Object>> trackList = tracks.get("tracks");
//            Allow the user to select a track, im just grabbing the first for this point
            Map <String, Object> firstTrack = trackList.get(0);
//            Get the track from the api
            Map <String, Object> trackInfo = api.getTrack((String) firstTrack.get("id"));
//            Insert the track into the database, this will eventually be handled by
//            "addTrackToPlaylist()" function, which checks if the track is already in the db
            sql.addUnseenTrack(trackInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

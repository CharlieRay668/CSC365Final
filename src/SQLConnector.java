import com.mysql.fabric.xmlrpc.base.Array;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
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
            stmt.executeQuery("SELECT * FROM chray.Genre WHERE name = '"+genre+"';");
            ResultSet rs = stmt.getResultSet();
            if (!rs.next()) {
                stmt.executeUpdate("INSERT INTO chray.Genre (name) VALUES ('"+genre+"');");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void linkArtistGenre(String artistID, String genre) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeQuery("SELECT * FROM chray.ArtistGenres WHERE aid = '"+artistID+"' AND genre = '"+genre+"';");
            ResultSet rs = stmt.getResultSet();
            if (!rs.next()) {
                stmt.executeUpdate("INSERT INTO chray.ArtistGenres (aid, genre) VALUES ('"+artistID+"','"+genre+"');");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void linkArtistAlbum(String artistID, String albumID) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeQuery("SELECT * FROM chray.ArtistAlbums WHERE aid = '"+artistID+"' AND albumID = '"+albumID+"';");
            ResultSet rs = stmt.getResultSet();
            if (!rs.next()) {
                stmt.executeUpdate("INSERT INTO chray.ArtistAlbums (aid, albumID) VALUES ('"+artistID+"','"+albumID+"');");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void linkPerformance(String artistID, String trackID) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeQuery("SELECT * FROM chray.Performances WHERE aid = '"+artistID+"' AND tid = '"+trackID+"';");
            ResultSet rs = stmt.getResultSet();
            if (!rs.next()) {
                stmt.executeUpdate("INSERT INTO chray.Performances (aid, tid) VALUES ('" + artistID + "','" + trackID + "');");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void linkAlbumTrack(String albumID, String trackID) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeQuery("SELECT * FROM chray.AlbumTracks WHERE albumID = '"+albumID+"' AND tid = '"+trackID+"';");
            ResultSet rs = stmt.getResultSet();
            if (!rs.next()) {
                stmt.executeUpdate("INSERT INTO chray.AlbumTracks (albumID, tid) VALUES ('"+albumID+"','"+trackID+"');");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertArtist(String artist, String artistID, int popularity, String uri, int followers) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeQuery("SELECT * FROM chray.Artist WHERE aid = '"+artistID+"';");
            ResultSet rs = stmt.getResultSet();
            if (!rs.next()) {
                stmt.executeUpdate("INSERT INTO chray.Artist (aid, name, popularity, uri, followers) VALUES ('"+artistID+"','"+artist+"',"+popularity+",'"+uri+"',"+followers+");");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void instertTrack(String tid, int duration, boolean explicit, String name, String uri) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeQuery("SELECT * FROM chray.Track WHERE tid = '"+tid+"';");
            ResultSet rs = stmt.getResultSet();
            if (!rs.next()) {
                stmt.executeUpdate("INSERT INTO chray.Track (tid, duration, explicit, name, uri) VALUES ('"+tid+"',"+duration+","+explicit+",'"+name+"','"+uri+"');");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void insertAlbum(String albumID, String type, int totalTracks, String name, String releaseDate, String uri) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeQuery("SELECT * FROM chray.Album WHERE albumID = '"+albumID+"';");
            ResultSet rs = stmt.getResultSet();
            if (!rs.next()) {
                stmt.executeUpdate("INSERT INTO chray.Album (albumID, type, totalTracks, name, releaseDate, uri) VALUES ('"+albumID+"','"+type+"',"+totalTracks+",'"+name+"','"+releaseDate+"','"+uri+"');");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void insertPlaylist(Integer pid, String name) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeQuery("SELECT * FROM chray.Playlist WHERE pid = '"+pid+"';");
            ResultSet rs = stmt.getResultSet();
            if (!rs.next()) {
                stmt.executeUpdate("INSERT INTO chray.Playlist (pid, name) VALUES ('"+pid+"','"+name+"');");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getTrackArtists(String tid) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeQuery("SELECT * FROM chray.Performances WHERE tid = '"+tid+"';");
            ResultSet rs = stmt.getResultSet();
            ArrayList<String> artists = new ArrayList<>();
            while (rs.next()) {
                String aid = rs.getString("aid");
                Statement stmt2 = this.connect.createStatement();
                stmt2.executeQuery("SELECT * FROM chray.Artist WHERE aid = '"+aid+"';");
                ResultSet rs2 = stmt2.getResultSet();
                rs2.next();
                artists.add(rs2.getString("name"));
            }
            return artists;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

    public void addTrackToPlaylist(Integer pid, String tid) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeQuery("SELECT * FROM chray.Track WHERE tid = '"+tid+"';");
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

    public int createPlaylist(String name) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeQuery("SELECT * FROM chray.Playlist WHERE name = '"+name+"';");
            ResultSet rs = stmt.getResultSet();
            if (!rs.next()) {
                stmt.executeUpdate("INSERT INTO chray.Playlist (name) VALUES ('"+name+"');");
                stmt.executeQuery("SELECT * FROM chray.Playlist WHERE name = '"+name+"';");
                rs = stmt.getResultSet();
                rs.next();
                return rs.getInt("pid");
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
    }

    public void playSong(String tid) {
        try {
            Statement stmt = this.connect.createStatement();
            Date date = new Date(System.currentTimeMillis());
            stmt.executeUpdate("INSERT INTO chray.Plays (tid, datePlayed) VALUES ('"+tid+"','"+date+"');");
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

    public Map<String, Object> grabSong(String tid) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeQuery("SELECT * FROM chray.Track WHERE tid = '"+tid+"';");
            ResultSet rs = stmt.getResultSet();
            rs.next();
            Map<String, Object> track = new HashMap<>();
            track.put("tid", tid);
            track.put("duration", rs.getInt("duration"));
            track.put("explicit", rs.getBoolean("explicit"));
            track.put("name", rs.getString("name"));
            track.put("uri", rs.getString("uri"));
            return track;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> grabAlbum(String albumID) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeQuery("SELECT * FROM chray.Album WHERE albumID = '"+albumID+"';");
            ResultSet rs = stmt.getResultSet();
            rs.next();
            Map<String, Object> album = new HashMap<>();
            album.put("albumID", albumID);
            album.put("type", rs.getString("type"));
            album.put("totalTracks", rs.getInt("totalTracks"));
            album.put("name", rs.getString("name"));
            album.put("releaseDate", rs.getString("releaseDate"));
            album.put("uri", rs.getString("uri"));
            return album;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> grabArtist(String aid) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeQuery("SELECT * FROM chray.Artist WHERE aid = '"+aid+"';");
            ResultSet rs = stmt.getResultSet();
            rs.next();
            Map<String, Object> artist = new HashMap<>();
            artist.put("aid", aid);
            artist.put("name", rs.getString("name"));
            artist.put("popularity", rs.getInt("popularity"));
            artist.put("uri", rs.getString("uri"));
            artist.put("followers", rs.getInt("followers"));
            return artist;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public ArrayList<Map<String, Object>> getPlaylistSongs(int pid) {
        try {
            Statement stmt = this.connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM chray.PlaylistTracks WHERE pid = '" + Integer.toString(pid) + "';");
            ArrayList<Map<String, Object>> tracks = new ArrayList<>();
            while (rs.next()) {
                String tid = rs.getString("tid");
                tracks.add(this.grabSong(tid));
            }
            rs.close();
            stmt.close();
            return tracks;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Map<String, Object>> filterPlaylistBy(int pid, String filterType, String filterValue) {
        try {
            ArrayList<Map<String, Object>> tracks = new ArrayList<>();
            if (filterType.equals("artist")) {
                String query = "SELECT * FROM chray.Track WHERE tid IN " +
                        "(SELECT tid FROM chray.PlaylistTracks WHERE pid = ? AND tid IN" +
                        "(SELECT tid FROM chray.Performances WHERE aid IN" +
                        "(SELECT aid FROM chray.Artist WHERE name = ?)));";
                PreparedStatement preparedStmt = this.connect.prepareStatement(query);
                preparedStmt.setInt(1, pid);
                preparedStmt.setString(2, filterValue);
                preparedStmt.executeQuery();
                ResultSet artistRS = preparedStmt.getResultSet();
                while (artistRS.next()) {
                    String tid = artistRS.getString("tid");
                    tracks.add(this.grabSong(tid));
                }
            } else if (filterType.equals("album")) {
                String query = "SELECT * FROM chray.Track WHERE tid IN " +
                        "(SELECT tid FROM chray.PlaylistTracks WHERE pid = ? AND tid IN" +
                        "(SELECT tid FROM chray.AlbumTracks WHERE albumID IN" +
                        "(SELECT albumID FROM chray.Album WHERE name = ?)));";
                PreparedStatement preparedStmt = this.connect.prepareStatement(query);
                preparedStmt.setInt(1, pid);
                preparedStmt.setString(2, filterValue);
                preparedStmt.executeQuery();
                ResultSet albumRS = preparedStmt.getResultSet();
                while (albumRS.next()) {
                    String tid = albumRS.getString("tid");
                    tracks.add(this.grabSong(tid));
                }
            }
            return tracks;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> mostListenedTrack(int pid) {
        try {
            Statement stmt = this.connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT tid, COUNT(*) AS count " +
                                                    "FROM chray.Plays " +
                                                    "WHERE tid IN (SELECT tid FROM chray.PlaylistTracks WHERE pid = '" + Integer.toString(pid) + "') " +
                                                    "GROUP BY tid ORDER BY count DESC LIMIT 1;");
            rs.next();
            String tid = rs.getString("tid");
            int count = rs.getInt("count");
            Map<String, Object> track = this.grabSong(tid);
            track.put("count", count);
            return track;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String mostListenedGenre(int pid) {
        try {
            Map<String, Object> mostListenedArtist = this.mostListenedArtist(pid);
            String aid = (String) mostListenedArtist.get("aid");
            Statement stmt = this.connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT genre, COUNT(*) AS count " +
                                                    "FROM chray.ArtistGenres " +
                                                    "WHERE aid = '" + aid + "' " +
                                                    "GROUP BY genre ORDER BY count DESC LIMIT 1;");
            rs.next();
            String genre = rs.getString("genre");
            return genre;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> mostListenedArtist(int pid) {
        try {
            Statement stmt = this.connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT aid, COUNT(*) AS count " +
                                                    "FROM chray.Performances " +
                                                    "WHERE aid IN (SELECT aid FROM chray.ArtistAlbums WHERE albumID IN " +
                                                                    "(SELECT albumID FROM chray.AlbumTracks WHERE tid IN " +
                                                                        "(SELECT tid FROM chray.PlaylistTracks WHERE pid = '" + Integer.toString(pid) + "'))) " +
                                                    "GROUP BY aid ORDER BY count DESC LIMIT 1;");
            rs.next();
            String aid = rs.getString("aid");
            int count = rs.getInt("count");
            Map<String, Object> artist = this.grabArtist(aid);
            artist.put("count", count);
            return artist;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> mostListenedAlbum(int pid) {
        try {
            Statement stmt = this.connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT albumID, COUNT(*) AS count " +
                                                    "FROM chray.AlbumTracks " +
                                                    "WHERE tid IN (SELECT tid FROM chray.PlaylistTracks WHERE pid = '" + Integer.toString(pid) + "') " +
                                                    "GROUP BY albumID ORDER BY count DESC LIMIT 1;");
            rs.next();
            String albumID = rs.getString("albumID");
            int count = rs.getInt("count");
            Map<String, Object> album = this.grabAlbum(albumID);
            album.put("count", count);
            return album;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String mostListenedDay(int pid) {
        try {
            Statement stmt = this.connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT datePlayed, COUNT(*) AS count " +
                                                    "FROM chray.Plays " +
                                                    "WHERE tid IN (SELECT tid FROM chray.PlaylistTracks WHERE pid = '" + Integer.toString(pid) + "') " +
                                                    "GROUP BY datePlayed ORDER BY count DESC LIMIT 1;");
            rs.next();
            String date = rs.getString("datePlayed");
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public long totalMinutesListened(int pid) {
        try {
            long totalmsListened = 0;
            Statement stmt = this.connect.createStatement();
//    Calculate based on plays table
            ResultSet rs = stmt.executeQuery(
            "SELECT SUM(T.duration) FROM" +
                    "Track AS T, Plays as P, PlaylistTracks AS PT" +
                    "WHERE T.tid = P.tid AND PT.pid = " + Integer.toString(pid) + " AND PT.tid = T.tid;");
            rs.next();
            totalmsListened += rs.getLong(1);
            return (long) (totalmsListened / 60000);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long playlistLength(int pid) {
        try {
            Statement stmt = this.connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * From chray.Track WHERE tid IN (SELECT tid FROM chray.PlaylistTracks WHERE pid = '" + Integer.toString(pid) + "');");
            int playlistLength = 0;
            while (rs.next()) {
                playlistLength += rs.getInt("duration");
            }
            return playlistLength/60000;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public ArrayList<String> getPlaylistNames() {
        try {
            Statement stmt = this.connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM chray.Playlist;");
            ArrayList<String> playlistNames = new ArrayList<>();
            while (rs.next()) {
                playlistNames.add(rs.getString("name"));
            }
            return playlistNames;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getpidFromPname(String name) {
        try {
            Statement stmt = this.connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM chray.Playlist WHERE name = '" + name + "';");
            rs.next();
            return rs.getInt("pid");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void main(String[] args) {
        APIPuller api = new APIPuller();
        SQLConnector sql = new SQLConnector();
        ArrayList<Map<String, Object>> playlistSongs = sql.getPlaylistSongs(4);
        for (Map<String, Object> track : playlistSongs) {
            System.out.println(track.get("name"));
        }
        ArrayList<Map<String, Object>> filteredArtist = sql.filterPlaylistBy(4, "artist", "Lil Uzi Vert");
        for (Map<String, Object> track : filteredArtist) {
            System.out.println(track.get("name"));
        }
        ArrayList<Map<String, Object>> filteredAlbum = sql.filterPlaylistBy(4, "album", "UTOPIA");
        for (Map<String, Object> track : filteredAlbum) {
            System.out.println(track.get("name"));
        }
        String mostListenedTrack = (String) sql.mostListenedTrack(4).get("name");
        System.out.println(mostListenedTrack);
        String mostListenedGenre = sql.mostListenedGenre(4);
        System.out.println(mostListenedGenre);
        String mostListenedArtist = (String) sql.mostListenedArtist(4).get("name");
        System.out.println(mostListenedArtist);
        String mostListenedAlbum = (String) sql.mostListenedAlbum(4).get("name");
        System.out.println(mostListenedAlbum);
        String mostListenedDay = sql.mostListenedDay(4);
        System.out.println(mostListenedDay);
        long totalMinutesListened = sql.totalMinutesListened(4);
        System.out.println(totalMinutesListened);
        long playlistLength = sql.playlistLength(4);
        System.out.println(playlistLength);

//        sql.clearTables();
//        System.out.println("Adding Songs");
//        ArrayList<String> desiredSongs = new ArrayList<>();
//        desiredSongs.add("Just Wanna Rock");
//        desiredSongs.add("I KNOW?");
//        desiredSongs.add("Yellow Brick Road");
//        int playlistID = sql.createPlaylist("Test Playlist");
//        for (String song : desiredSongs) {
//            try {
//                Map<String, ArrayList<Map<String, Object>>> tracks = api.querySpotify(song);
//                ArrayList<Map<String, Object>> trackList = tracks.get("tracks");
//                Map<String, Object> firstTrack = trackList.get(0);
//                sql.addTrackToPlaylist(playlistID, (String) firstTrack.get("id"));
//                for (int i = 0; i < 10; i++) {
//                    sql.playSong((String) firstTrack.get("id"));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

//        try {
//            sql.clearTables();
////            Query string
//            String track = "Just Wanna Rock";
////            Hit the backend
//            Map<String, ArrayList<Map<String, Object>>> tracks = api.querySpotify(track);
////            Display the tracks
//            ArrayList<Map<String, Object>> trackList = tracks.get("tracks");
////            Allow the user to select a track, im just grabbing the first for this point
//            Map <String, Object> firstTrack = trackList.get(0);
////            Get the track from the api
//            Map <String, Object> trackInfo = api.getTrack((String) firstTrack.get("id"));
////            Insert the track into the database, this will eventually be handled by
////            "addTrackToPlaylist()" function, which checks if the track is already in the db
//            sql.addUnseenTrack(trackInfo);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}

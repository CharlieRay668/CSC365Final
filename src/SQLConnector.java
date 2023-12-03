import java.sql.*;

class SQLConnector {
    static private Connection connect;

    public SQLConnector() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connect = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306?user=chray&password=talltree");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertGenre(String genre) {
        try {
            Statement stmt = this.connect.createStatement();
            stmt.executeUpdate("INSERT INTO chray.genres (genre) VALUES ('"+genre+"');");
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

    public static void insertAlbum(String albumID, String type, int totalTracks, String name, String releaseDate, String uri) {
        try {
            Statement stmt = connect.createStatement();
            stmt.executeUpdate("INSERT INTO chray.Album (albumID, type, totalTracks, name, releaseDate, uri) VALUES ('"+albumID+"','"+type+"',"+totalTracks+",'"+name+"','"+releaseDate+"','"+uri+"');");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void insertPlaylist(String pid, String name) {
        try {
            Statement stmt = connect.createStatement();
            stmt.executeUpdate("INSERT INTO chray.Playlist (pid, name) VALUES ('"+pid+"','"+name+"');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

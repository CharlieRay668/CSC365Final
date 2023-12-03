//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.control.Label;
//import javafx.scene.layout.StackPane;
//import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Main {//} extends Application {

//    @Override
//    public void start(Stage stage) {
//        String javaVersion = System.getProperty("java.version");
//        String javafxVersion = System.getProperty("javafx.version");
//        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
//        Scene scene = new Scene(new StackPane(l), 640, 480);
//        stage.setScene(scene);
//        stage.show();
//    }

    // This is just a demo of how the backend works
    public static void main(String[] args) throws IOException, InterruptedException{
        SQLConnector sql = new SQLConnector();
        // We should recieve 3 strings from the frontend when searching for a song/album/artist
        String track = "just wanna rock";
        String artist = "";
        String album = "";
        APIPuller api = new APIPuller();
        // This returns a map of the query results ex:
        //  {"tracks": [track1, track2, track3], "albums": [album1, album2, album3], "artists": [artist1, artist2, artist3]"}
        Map<String, ArrayList<Map<String, Object>>> querySet = api.querySpotify(artist, album, track);
        // Im just grabbing the first track returned for demonstration purposes
        ArrayList<Map<String, Object>> tracks = querySet.get("tracks");
        Map<String, Object> firstTrack = tracks.get(0);
        // This adds the track to the database, with the assumption that we have never seen this track before, we will need to add a check for that
        sql.addUnseenTrack(firstTrack);
        // Currently it looks like it populates the Artist Track and Album tables mostly correctly
        // The artist "popularity" and "followers" seem to be parsing incorreclty, but im not even sure if we need those -CR
//        sql.clearTables();
    }

}
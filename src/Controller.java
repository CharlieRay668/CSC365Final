
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class Controller {
    @FXML
    TextField trackString;
    @FXML
    ListView mainSearchResults;
    @FXML
    private MenuButton addToPlaylistButton;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private String trackResult;//holds the trackId of selected song to add
    @FXML
    public void initialize(){
        List<String> playlistNames= Arrays.asList("one","two","threegvgvvyuvyuvyvyvyvy");
        populateAddToPlaylistMenu(playlistNames);

    }

    // Call this method to populate the MenuButton items
    public void populateAddToPlaylistMenu(List<String> playlistNames) {
        addToPlaylistButton.getItems().clear(); // Clear existing items if needed

        for (String playlist : playlistNames) {
            MenuItem item = new MenuItem(playlist);
            item.setOnAction(event -> handleSelectedPlaylistItem(playlist));
            addToPlaylistButton.getItems().add(item);
        }
    }
    private void handleSelectedPlaylistItem(String playlistName) {
        System.out.println("Playlist selected: " + playlistName);
        // Store the playlist name or perform other actions as needed
        //after inserting the trackresult into playlistName, find a way
        //to clear the search bar and clear searchResultList so it resets and looks like the action was completed
    }
    //method grabs the entered track string, queries the api, fills the resultList with results
    public void searchMain(ActionEvent e) throws IOException, InterruptedException {
        String track = trackString.getText();
        APIPuller api =new APIPuller();
        Map<String, ArrayList<Map<String, Object>>> results =api.querySpotify(track);
        ArrayList<Map<String, Object>> tracks = results.get("tracks");
        ObservableList<TrackResult> searchResults = FXCollections.observableArrayList();
        for (Map<String, Object> trackMap : tracks) {
            String trackID2 = (String) trackMap.get("id");
            Map<String, Object> fullTrack = api.getTrack(trackID2);
            ArrayList<Map<String, Object>> artists = (ArrayList<Map<String, Object>>) fullTrack.get("artists");
            Map<String, Object> trackResult = (Map<String, Object>) fullTrack.get("track");
            String entryName = "";
            entryName += (String) trackResult.get("name");
            for (Map<String, Object> artist : artists) {
                entryName += ", " + (String) artist.get("name");
            }
            TrackResult entry = new TrackResult(entryName, trackID2);
            searchResults.add(entry);
        }
        mainSearchResults.setItems(searchResults);
    }
    public void switchToPlaylists(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("playlists.fxml"));
        stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        scene =new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        scene =new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToPlaylistContents(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("playlistContents.fxml"));
        stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        scene =new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

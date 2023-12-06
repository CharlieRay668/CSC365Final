
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class Controller {
    @FXML
    ProgressIndicator progressWheel;
    @FXML
    TextField limitString;
    @FXML
    TextField trackString;
    @FXML
    ListView mainSearchResults;
    @FXML
    private MenuButton addToPlaylistButton;
    private Stage stage;
    private Scene scene;
    private Parent root;
    SQLConnector connector = new SQLConnector();
    private String trackResult;//holds the trackId of selected song to add
    @FXML
    public void initialize(){
        List<String> playlists =connector.getPlaylistNames();
        populateAddToPlaylistMenu(playlists);

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
        int pid = connector.getpidFromPname(playlistName);
        connector.addTrackToPlaylist(pid,trackResult);
        // Store the playlist name or perform other actions as needed
        //after inserting the trackresult into playlistName, find a way
        //to clear the search bar and clear searchResultList so it resets and looks like the action was completed
        mainSearchResults.getItems().clear();
    }

    public void searchMain(ActionEvent e) {
        progressWheel.setVisible(true);
        String track = trackString.getText();
        String limString = limitString.getText();
        // Convert limit to an integer, defaulting to 0 if empty
        int limit = (limString != null && !limString.isEmpty()) ? Integer.parseInt(limString) : 0;
        // Background task for running the search
        Task<ObservableList<TrackResult>> searchTask = new Task<ObservableList<TrackResult>>() {
            @Override
            protected ObservableList<TrackResult> call() throws Exception {
                APIPuller api =new APIPuller();
                Map<String, ArrayList<Map<String, Object>>> results =api.querySpotify(track,limit);
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
                return searchResults;
            }
        };
        // Event handler for when the task succeeds
        searchTask.setOnSucceeded(event -> {
            mainSearchResults.setItems(searchTask.getValue());
            progressWheel.setVisible(false);
        });
        // Event handler for when the task fails
        searchTask.setOnFailed(event -> {
            // Handle any exceptions here
            progressWheel.setVisible(false);
        });
        // Start the task on a new thread
        new Thread(searchTask).start();
    }

    @FXML
    public void chooseTrack(MouseEvent arg0) {
        TrackResult entry = (TrackResult) mainSearchResults.getSelectionModel().getSelectedItem();
        trackResult = entry.getId();
        System.out.println(trackResult);

    }


    public void switchToPlaylists(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("playlists.fxml"));
        stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        scene =new Scene(root);
        stage.setScene(scene);
        stage.show();
    }




}

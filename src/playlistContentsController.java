import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class playlistContentsController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    SQLConnector connector =new SQLConnector();

    @FXML
    Label playlistName;
    String globalPlaylistName;

    @FXML
    ComboBox filterTypeBox;

    @FXML
    TextField filterValueField;

    @FXML
    Label mostListenedTrack;

    @FXML
    Label mostListenedArtist;

    @FXML
    Label mostListenedAlbum;

    @FXML
    Label mostListenedGenre;

    @FXML
    Label mostListenedDay;

    @FXML
    Label totalMinutesListened;

    @FXML
    Label playlistLength;

    String selectedSong;

    @FXML
    ListView songList;

    public void displayPlaylistName(String name){
        globalPlaylistName = name;
        playlistName.setText("Playlist: "+name);
        filterTypeBox.getItems().addAll(
                "Artist",
                "Album"
        );
        fillSongList(name);
        getData();
    }
    @FXML
    public void pickSong(MouseEvent arg0) {
        TrackResult entry = (TrackResult) songList.getSelectionModel().getSelectedItem();
        selectedSong = entry.getId();
        System.out.println(selectedSong);
    }
    public void playSong(ActionEvent e){
        connector.playSong(selectedSong);
        getData();
    }

    public void getData() {
        displayMostListenedTrack();
        displayMostListenedArtist();
        displayMostListenedAlbum();
        displayMostListenedGenre();
        displayMostListenedDay();
        displayTotalMinutesListened();
        displayPlaylistLength();
    }
    public void resetSongs(ActionEvent e){
        fillSongList(globalPlaylistName);
    }
    public void fillSongList(String playlistName){
        int pid = connector.getpidFromPname(playlistName);
        List<Map<String, Object>> tracks = connector.getPlaylistSongs(pid);
        ObservableList<TrackResult> trackNames = FXCollections.observableArrayList();
        for (Map<String, Object> track : tracks) {
            String entry = (String) track.get("name");
            String tid = (String) track.get("tid");
            ArrayList<String> artists = connector.getTrackArtists(tid);
            for (String artist : artists) {
                entry += " - " + artist;
            }
            TrackResult entry2 = new TrackResult(entry, tid);
            trackNames.add(entry2);
        }
        songList.setItems(trackNames);
    }

    public void filter() {
        String filterType = (String) filterTypeBox.getValue();
        String filterValue = filterValueField.getText();
        if (filterType.equals("Artist")) {
            filterByArtist(filterValue);
        } else if (filterType.equals("Album")) {
            filterByAlbum(filterValue);
        }
    }
    public void filterByArtist(String artistName) {
        int pid = connector.getpidFromPname(globalPlaylistName);
        List<Map<String, Object>> tracks = connector.filterPlaylistBy(pid, "artist", artistName);
        ObservableList<TrackResult> trackNames = FXCollections.observableArrayList();
        for (Map<String, Object> track : tracks) {
            String entry = (String) track.get("name");
            ArrayList<String> artists = connector.getTrackArtists((String) track.get("tid"));
            for (String artist : artists) {
                entry += " - " + artist;
            }
            TrackResult entry2 = new TrackResult(entry, (String) track.get("tid"));
            trackNames.add(entry2);
        }
        songList.setItems(trackNames);
    }

    public void filterByAlbum(String albumName) {
        int pid = connector.getpidFromPname(globalPlaylistName);
        List<Map<String, Object>> tracks = connector.filterPlaylistBy(pid, "album", albumName);
        ObservableList<TrackResult> trackNames = FXCollections.observableArrayList();
        for (Map<String, Object> track : tracks) {
            String entry = (String) track.get("name");
            ArrayList<String> artists = connector.getTrackArtists((String) track.get("tid"));
            for (String artist : artists) {
                entry += " - " + artist;
            }
            TrackResult entry2 = new TrackResult(entry, (String) track.get("tid"));
            trackNames.add(entry2);
        }
        songList.setItems(trackNames);
    }

    public void displayMostListenedTrack() {
        int pid = connector.getpidFromPname(globalPlaylistName);
        Map<String, Object> track = connector.mostListenedTrack(pid);
        String entry = (String) track.get("name");
        ArrayList<String> artists = connector.getTrackArtists((String) track.get("tid"));
        for (String artist : artists) {
            entry += " - " + artist;
        }
        mostListenedTrack.setText(entry);
    }

    public void displayMostListenedArtist() {
        int pid = connector.getpidFromPname(globalPlaylistName);
        Map<String, Object> artist = connector.mostListenedArtist(pid);
        String entry = (String) artist.get("name");
        mostListenedArtist.setText(entry);
    }

    public void displayMostListenedAlbum() {
        int pid = connector.getpidFromPname(globalPlaylistName);
        Map<String, Object> album = connector.mostListenedAlbum(pid);
        String entry = (String) album.get("name");
        mostListenedAlbum.setText(entry);
    }

    public void displayMostListenedGenre() {
        int pid = connector.getpidFromPname(globalPlaylistName);
        String genre = connector.mostListenedGenre(pid);
        mostListenedGenre.setText(genre);
    }

    public void displayMostListenedDay() {
        int pid = connector.getpidFromPname(globalPlaylistName);
        String day = connector.mostListenedDay(pid);
        mostListenedDay.setText(day);
    }

    public void displayTotalMinutesListened() {
        int pid = connector.getpidFromPname(globalPlaylistName);
        int minutes = connector.totalMinutesListened(pid);
        totalMinutesListened.setText(Integer.toString(minutes));
    }

    public void displayPlaylistLength() {
        int pid = connector.getpidFromPname(globalPlaylistName);
        int length = connector.playlistLength(pid);
        playlistLength.setText(Integer.toString(length));
    }

    public void switchToMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        scene =new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToPlaylists(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("playlists.fxml"));
        stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        scene =new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

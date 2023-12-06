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



public class playlistController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    String playlistName;
    SQLConnector connector =new SQLConnector();
    ObservableList<String> playlists = FXCollections.observableArrayList();
    @FXML
    TextField newPlaylistName;
    @FXML
    ListView allPlaylists;
    @FXML
    public void initialize(){
        ArrayList<String> tempPlaylists = connector.getPlaylistNames();
        for (String playlistName : tempPlaylists) {
            playlists.add(playlistName);
        }
        allPlaylists.setItems(playlists);

    }
    public void switchToMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage =(Stage)((Node)event.getSource()).getScene().getWindow();
        scene =new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void createNewPlaylist(ActionEvent e){
        String playlistName = newPlaylistName.getText();
        if (playlistName.isEmpty()){
            return;
        }
        if (playlists.contains(playlistName)) {
            return;
        }
        connector.createPlaylist(playlistName);
        playlists.add(playlistName);
        allPlaylists.setItems(playlists);

    }
    @FXML
    public void choosePlaylist(MouseEvent arg0) throws IOException {
        String entry = (String) allPlaylists.getSelectionModel().getSelectedItem();
        playlistName = entry;
        System.out.println(entry);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("playlistContents.fxml"));
        root =loader.load();
        playlistContentsController playlistContentsController =loader.getController();
        playlistContentsController.displayPlaylistName(entry);



        stage =(Stage)((Node)arg0.getSource()).getScene().getWindow();
        scene =new Scene(root);
        stage.setScene(scene);
        stage.show();

    }


}

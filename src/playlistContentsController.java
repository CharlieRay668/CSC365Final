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

    @FXML
    ListView songList;

    public void displayPlaylistName(String name){
        playlistName.setText(name);
    }
    public void fillSongList(String albumName){


       // songList.setItems();
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

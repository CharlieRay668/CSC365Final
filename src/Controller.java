
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    @FXML
    TextField trackString;
    private Stage stage;
    private Scene scene;
    private Parent root;
    public void searchMain(ActionEvent e){
        String track = trackString.getText();
        System.out.println(track);
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

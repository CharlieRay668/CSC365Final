import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;


public class Main extends Application {


    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Spotify Search Engine");
        Image icon =new Image("logo.png");
        //sets title,window size, and label
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }


    public void startTest(Stage stage)throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, Color.LIGHTSLATEGREY);
        Image icon =new Image("logo.png");
        //sets title,window size, and label
        stage.getIcons().add(icon);
        stage.setTitle("Spotify search engine");
        stage.setHeight(900);
        stage.setWidth(1200);
        Label titleLabel = new Label("Search Tracks");
        titleLabel.setFont(new Font("Mono Script", 32));

        //sets search fields and search button
        TextField trackField = new TextField();
        trackField.setPromptText("Enter Track Name");

        TextField artistField = new TextField();
        artistField.setPromptText("Enter Artist Name");

        TextField albumField = new TextField();
        albumField.setPromptText("Enter Album Name");

        Button searchButton = new Button("Search");
        //^^^^^^^^

        ListView<String> resultList = new ListView<>();
        //sets horizontal search bars in a hbox
        HBox searchFields = new HBox(10); // 10 is the spacing between elements
        searchFields.getChildren().addAll(trackField, artistField, albumField);
        //grabs the hbox and scrren title and search button and displays in a vbox
        VBox layout = new VBox(10); // 10 is the spacing
        layout.getChildren().addAll(titleLabel,searchFields, searchButton, resultList);
        root.getChildren().add(layout);

        searchButton.setOnAction(event -> {
            String track = trackField.getText();
            String artist = artistField.getText();
            String album = albumField.getText();
            System.out.println(track);
            // Backend call
            // List<String> searchResults = something


            //resultList.getItems().clear();
            //resultList.getItems().addAll(searchResults);
        });
        //stage.setScene(scene);
        //stage.show();
    }
    public static void main(String[] args) {
        System.out.println("Hello world!");
        launch(args);
    }
}

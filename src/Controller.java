
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    TextField trackString;
    public void searchMain(ActionEvent e){
        String track = trackString.getText();

    }
}

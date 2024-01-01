package proyecto.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.layout.*;

public class MenuController implements Initializable {
    @FXML
    private VBox box;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        URL cssURL = App.class.getResource("menu.css");
        String urlString = cssURL.toString();
        box.getStylesheets().add(urlString);
    }

    public void handleSingleplayer() throws IOException {
        App.setRoot("singleplayer");
    }
}

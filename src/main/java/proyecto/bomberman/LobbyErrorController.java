package proyecto.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.layout.*;

public class LobbyErrorController implements Initializable {
  @FXML private VBox box;
  @FXML private VBox content;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    URL cssURL = App.class.getResource("lobbyError.css");
    String urlString = cssURL.toString();
    box.getStylesheets().add(urlString);

    content.prefHeightProperty().bind(App.getStage().heightProperty());
  }

  @FXML
  private void switchToPrimary() throws IOException {
    App.setRoot("primary");
  }
}

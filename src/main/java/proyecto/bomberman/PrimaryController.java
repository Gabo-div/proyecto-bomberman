package proyecto.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.layout.*;

public class PrimaryController implements Initializable {
  @FXML private VBox box;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    URL cssURL = App.class.getResource("primary.css");
    String urlString = cssURL.toString();
    box.getStylesheets().add(urlString);
  }

  @FXML
  private void switchToSinglePlayer() throws IOException {
    App.setRoot("singleplayer");
  }

  @FXML
  private void switchToMultiplayer() throws IOException {
    App.setRoot("multiplayerMenu");
  }

  @FXML
  public void Exit() {
    App.getStage().close();
    System.exit(0);
  }
}

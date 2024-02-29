package proyecto.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.layout.*;

public class MultiplayerMenuController implements Initializable {
  @FXML private VBox box;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    URL cssURL = App.class.getResource("multiplayerMenu.css");
    String urlString = cssURL.toString();
    box.getStylesheets().add(urlString);
  }

  @FXML
  private void switchToHostParty() throws IOException {
    App.setRoot("hostParty");
  }

  @FXML
  private void switchToJoinLobby() throws IOException {
    App.setRoot("joinlobby");
  }

  @FXML
  private void switchToPrimary() throws IOException {
    App.setRoot("primary");
  }
}

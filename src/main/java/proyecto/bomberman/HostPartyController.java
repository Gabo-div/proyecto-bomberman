package proyecto.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

public class HostPartyController implements Initializable {
  @FXML private VBox box;
  @FXML private TextField nicknameField;
  @FXML private ChoiceBox<Integer> roomSizeChoice;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    URL cssURL = App.class.getResource("hostParty.css");
    String urlString = cssURL.toString();
    box.getStylesheets().add(urlString);

    roomSizeChoice.getItems().addAll(2, 3, 4);
    roomSizeChoice.setValue(2);
  }

  @FXML
  private void createParty() throws IOException {
    App.setRoot("hostLobbyRoom");
    Initializable controller = App.getCurrentController();

    if (controller instanceof HostLobbyController) {
      ((HostLobbyController)controller).setNickname(nicknameField.getText());
      ((HostLobbyController)controller).setRoomSize(roomSizeChoice.getValue());
    }
  }

  @FXML
  private void switchToMultiplayer() throws IOException {
    App.setRoot("multiplayerMenu");
  }
}

package proyecto.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

public class JoinPartyController implements Initializable {
  @FXML private VBox box;
  @FXML private TextField nicknameField;
  @FXML private TextField portField;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    URL cssURL = App.class.getResource("hostParty.css");
    String urlString = cssURL.toString();
    box.getStylesheets().add(urlString);
  }

  @FXML
  private void joinParty() throws IOException {
    String nickname = nicknameField.getText().trim();
    String port = portField.getText().trim();

    if (nickname.isEmpty() || port.isEmpty()) {
      return;
    }

    try {
      Integer portNumber = Integer.parseInt(port);

      App.setRoot("lobbyRoom");
      Initializable controller = App.getCurrentController();

      if (controller instanceof LobbyController) {
        ((LobbyController)controller).setNickname(nickname);
        ((LobbyController)controller).setPort(portNumber);
      }

    } catch (NumberFormatException e) {
      return;
    }
  }

  @FXML
  private void switchToMultiplayer() throws IOException {
    App.setRoot("multiplayerMenu");
  }
}

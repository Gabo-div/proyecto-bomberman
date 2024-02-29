package proyecto.bomberman;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SwitchController {

  Stage stage;

  @FXML private AnchorPane scenePane;
  @FXML private Button logoutButton;
  @FXML private static TextField Nickname;

  public static String getNickname() {

    String nickname = Nickname.getText();
    return nickname;
  }

  @FXML
  private void switchToMultiplayer() throws IOException {
    App.setRoot("multiplayerMode");
  }

  @FXML
  private void switchToPrimary() throws IOException {
    App.setRoot("primary");
  }

  @FXML
  private void switchToSinglePlayer() throws IOException {
    App.setRoot("singleplayer");
  }

  @FXML
  private void switchToHostParty() throws IOException {
    App.setRoot("hostParty");
  }

  @FXML
  private void switchToJoinLobby() throws IOException {

    App.setRoot("Joinlobby");
  }

  @FXML
  private void switchToJoinParty() throws IOException {}

  @FXML
  public void Exit(ActionEvent ActionEvent) {
    stage = (Stage)scenePane.getScene().getWindow();
    stage.close();
  }
}

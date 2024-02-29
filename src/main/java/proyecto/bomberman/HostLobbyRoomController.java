package proyecto.bomberman;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class HostLobbyRoomController implements Initializable {
  @FXML private VBox box;
  @FXML private Label nicknameLabel;
  @FXML private Label roomSizeLabel;

  String nickname;
  Integer roomSize;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    URL cssURL = App.class.getResource("hostLobbyRoom.css");
    String urlString = cssURL.toString();
    box.getStylesheets().add(urlString);
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
    nicknameLabel.setText(nickname);
  }

  public void setRoomSize(Integer roomSize) {
    this.roomSize = roomSize;
    roomSizeLabel.setText(roomSize.toString());
  }
}

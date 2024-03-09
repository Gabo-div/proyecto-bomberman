package proyecto.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import proyecto.model.Sprite;
import proyecto.model.SpriteSheet;
import proyecto.multiplayer.CharacterColor;
import proyecto.multiplayer.GameClient;
import proyecto.multiplayer.ServerState;
import proyecto.multiplayer.User;

public class LobbyController implements Initializable {

  @FXML private VBox box;
  @FXML private VBox content;
  @FXML private VBox loading;
  @FXML private VBox players;
  @FXML private VBox chat;

  @FXML private Label label_room;

  @FXML private Button button_send;
  @FXML private TextField tf_message;
  @FXML private VBox vbox_messages;
  @FXML private ScrollPane sp_main;
  @FXML private static GridPane gridpane;
  @FXML private static TextField tf_username;

  @FXML private Button button_start;

  private GameClient client = GameClient.getInstance();
  private String nickname;
  private Integer port;

  private Stage stage = App.getStage();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    URL cssURL = App.class.getResource("lobbyRoom.css");
    String urlString = cssURL.toString();
    box.getStylesheets().add(urlString);

    loading.prefHeightProperty().bind(stage.heightProperty());
    content.prefHeightProperty().bind(stage.heightProperty());
    loading.managedProperty().bind(loading.visibleProperty());
    content.managedProperty().bind(content.visibleProperty());

    loading.setVisible(true);
    content.setVisible(false);

    button_start.setDisable(true);

    if (client.getState() == ServerState.CONNECTED) {
      updateUsers(client.getUsers());
      startListeners();
      label_room.setText("Sala: " + client.getPort());
      loading.setVisible(false);
      content.setVisible(true);
      return;
    }

    waitForData();
  }

  private void waitForData() {
    new Thread(() -> {
      while (true) {
        if (nickname == null || port == null) {
          try {
            Thread.sleep(100);
          } catch (Exception e) {
            e.printStackTrace();
          }
          continue;
        }

        Platform.runLater(() -> {
          startListeners();
          client.start(port);
          label_room.setText("Sala: " + port);
        });

        break;
      }
    }).start();
  }

  private void startListeners() {
    client.setOnStateChange((state) -> {
      if (state == ServerState.CONNECTING) {
        loading.setVisible(true);
        content.setVisible(false);
      }

      if (state == ServerState.CONNECTED) {
        loading.setVisible(false);
        content.setVisible(true);
        client.joinLobby(nickname);
      };

      if (state == ServerState.INGAME) {
        try {
          App.setRoot("multiplayer");
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      if (state == ServerState.ERROR || state == ServerState.DISCONNECTED) {
        try {
          App.setRoot("lobbyError");
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    client.setOnUsersChange(
        (users) -> { Platform.runLater(() -> { updateUsers(users); }); });

    client.setOnMessage((message) -> {
      String messageToSend = message.getMessage();
      User user = message.getUser();
      String name = user.getName();

      Pos position = Pos.TOP_LEFT;
      String styles =
          "-fx-background-color: rgb(213, 213, 215); -fx-background-radius: 20px;";
      Color fill = Color.BLACK;
      String textMsg = name + ": " + messageToSend;

      if (name.equals(client.getClientUser().getName())) {
        position = Pos.TOP_RIGHT;
        styles =
            "-fx-background-color: rgb(50, 50, 50); -fx-background-radius: 20px; -fx-padding: 5;";
        fill = Color.WHITE;
        textMsg = messageToSend;
      }

      HBox hbox = new HBox();
      hbox.setAlignment(position);
      hbox.setPadding(new Insets(5, 5, 5, 10));

      Text text = new Text(textMsg);
      text.setFill(fill);
      TextFlow textFlow = new TextFlow(text);
      textFlow.setStyle(styles);

      textFlow.setPadding(new Insets(5, 10, 5, 10));
      hbox.getChildren().add(textFlow);

      Platform.runLater(() -> { vbox_messages.getChildren().add(hbox); });
    });
  }

  private void updateUsers(List<User> users) {
    players.getChildren().clear();

    for (User user : users) {
      CharacterColor color = user.getColor();
      String characterColor = color.toString().toLowerCase();
      Sprite sprite =
          SpriteSheet.getInstance().getSprite(characterColor + "_face");
      ImageView imageView = new ImageView(sprite.getImage());
      imageView.setFitHeight(24);
      imageView.setFitWidth(24);

      Text text = new Text(user.getName());
      text.setFill(Color.BLACK);

      HBox hbox = new HBox();
      hbox.setPadding(new Insets(5, 10, 5, 10));
      hbox.setAlignment(Pos.CENTER);

      hbox.getChildren().add(imageView);
      hbox.getChildren().add(text);

      players.getChildren().add(hbox);
    }
  }

  @FXML
  public void sendMessage() {
    String messageToSend = tf_message.getText();

    if (messageToSend.isEmpty()) {
      return;
    }

    tf_message.clear();
    client.sendMessage(messageToSend);
  }

  @FXML
  public void changeColor(ActionEvent event) {
    String color = ((Button)event.getSource()).getText();
    CharacterColor characterColor = CharacterColor.valueOf(color);

    client.changeColor(characterColor);
  }

  @FXML
  public void switchToPrimary() throws IOException {
    App.setRoot("primary");
    client.stop();
  }

  public void setNickname(String nickname) { this.nickname = nickname; }

  public void setPort(int port) { this.port = port; }
}

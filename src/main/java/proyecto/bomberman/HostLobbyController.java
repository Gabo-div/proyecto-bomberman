package proyecto.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import proyecto.model.Sprite;
import proyecto.model.SpriteSheet;
import proyecto.multiplayer.CharacterColor;
import proyecto.multiplayer.GameServer;
import proyecto.multiplayer.ServerState;
import proyecto.multiplayer.User;

/**
 * Controlador para la vista de la sala de fiestas del anfitrión en el juego.
 */
public class HostLobbyController implements Initializable {

  /** Contenedor de diseño vertical. */
  @FXML private VBox box;

  /** Contenedor de contenido principal. */
  @FXML private VBox content;

  /** Contenedor de jugadores en la sala. */
  @FXML private VBox players;

  /** Contenedor de chat en la sala. */
  @FXML private VBox chat;

  /** Etiqueta para mostrar el número de sala. */
  @FXML private Label label_room;

  /** Botón para enviar mensajes en el chat. */
  @FXML private Button button_send;

  /** Campo de texto para ingresar mensajes en el chat. */
  @FXML private TextField tf_message;

  /** Contenedor de mensajes en el chat. */
  @FXML private VBox vbox_messages;

  /** ScrollPane para desplazar los mensajes en el chat. */
  @FXML private ScrollPane sp_main;

  /** Cuadrícula para la disposición de elementos. */
  @FXML private static GridPane gridpane;

  /** Campo de texto para ingresar el nombre de usuario. */
  @FXML private static TextField tf_username;

  @FXML private Button button_start;

  /** Apodo del usuario anfitrión. */
  private String nickname;

  /** Tamaño de la sala. */
  private Integer roomSize;

  /** Instancia del escenario de la aplicación. */
  private Stage stage = App.getStage();

  /** Instancia del servidor del juego. */
  private GameServer server = GameServer.getInstance();

  /**
   * Inicializa el controlador después de que se haya cargado la vista.
   * @param url La ubicación relativa del archivo FXML.
   * @param rb Recursos específicos del idioma.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // Cargar la hoja de estilo CSS para la sala de fiestas del anfitrión
    URL cssURL = App.class.getResource("hostLobbyRoom.css");
    String urlString = cssURL.toString();
    box.getStylesheets().add(urlString);
    button_start.setDisable(true);

    // Ajustar la altura del contenedor de contenido principal
    content.prefHeightProperty().bind(stage.heightProperty());

    if (server.getState() == ServerState.CONNECTED) {
      updateUsers(server.getUsers());
      startListeners();
      label_room.setText("Sala: " + server.getPort());
      return;
    }

    waitForData();
  }

  /**
   * Espera hasta que se obtengan los datos necesarios (apodo y tamaño de la sala).
   */
  private void waitForData() {
    new Thread(() -> {
      while (true) {
        // Verificar si se han obtenido el apodo y el tamaño de la sala
        if (nickname == null || roomSize == null) {
          try {
            Thread.sleep(100);
          } catch (Exception e) {
            e.printStackTrace();
          }
          continue;
        }

        // Una vez que se obtienen los datos, iniciar el servidor y los escuchadores
        Platform.runLater(() -> {
          server.start(roomSize);
          startListeners();

          // Establecer al usuario anfitrión de la sala
          User hostUser = new User(nickname, CharacterColor.WHITE);
          server.setHost(hostUser);

          // Mostrar el número de sala en la etiqueta correspondiente
          label_room.setText("Sala: " + server.getPort());
        });

        break;
      }
    }).start();
  }

  /**
   * Inicia los escuchadores para los cambios de usuarios y mensajes en el servidor.
   */
  private void startListeners() {
    server.setOnStateChange((state) -> {
      Platform.runLater(() -> {
        if (state == ServerState.INGAME) {
          try {
            App.setRoot("hostMultiplayer");
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      });
    });

    server.setOnUsersChange(
        (users) -> { Platform.runLater(() -> { updateUsers(users); }); });

    server.setOnMessage((message) -> {
      // Obtener el mensaje y el usuario que lo envió
      String messageToSend = message.getMessage();
      User user = message.getUser();
      String name = user.getName();

      // Determinar la posición del mensaje en función del usuario
      Pos position = Pos.TOP_LEFT;
      String styles = "-fx-background-color: rgb(213, 213, 215); -fx-background-radius: 20px;";
      Color fill = Color.BLACK;
      String textMsg = name + ": " + messageToSend;

      if (name.equals(server.getHost().getName())) {
        position = Pos.TOP_RIGHT;
        styles = "-fx-background-color: rgb(50, 50, 50); -fx-background-radius: 20px; -fx-padding: 5;";
        fill = Color.WHITE;
        textMsg = messageToSend;
      }

      // Crear el nodo de texto con estilo y agregarlo al contenedor de mensajes
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

  /**
   * Actualiza la lista de usuarios en la sala.
   * @param users La lista de usuarios en la sala.
   */
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

    if (users.size() >= 2) {
      button_start.setDisable(false);
    } else {
      button_start.setDisable(true);
    }
  }

  /**
   * Envía un mensaje en el chat.
   */
  @FXML
  public void sendMessage() {
    String messageToSend = tf_message.getText();

    if (messageToSend.isEmpty()) {
      return;
    }

    tf_message.clear();
    server.sendMessage(messageToSend);
  }

  /**
   * Cambia el color del personaje del anfitrión.
   * @param event El evento que desencadena el cambio de color.
   */
  @FXML
  public void changeColor(ActionEvent event) {
    String color = ((Button)event.getSource()).getText();
    CharacterColor characterColor = CharacterColor.valueOf(color);

    server.changeColor(characterColor);
  }

  /**
   * Cambia a la vista principal y detiene el servidor.
   * @throws IOException si hay un error al cambiar la vista.
   */
  @FXML
  public void switchToPrimary() throws IOException {
    server.stop();
    App.setRoot("primary");
  }

  /**
   * Inicia el juego.
   */
  @FXML
  public void startGame() {
    server.startGame();
  }

  /**
   * Establece el apodo del usuario anfitrión.
   * @param nickname El apodo del usuario anfitrión.
   */
  public void setNickname(String nickname) { this.nickname = nickname; }

  /**
   * Establece el tamaño de la sala.
   * @param roomSize El tamaño de la sala.
   */
  public void setRoomSize(Integer roomSize) { this.roomSize = roomSize; }
}

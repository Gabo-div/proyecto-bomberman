package proyecto.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

/**
 * Controlador para la vista de crear una fiesta en el juego Bomberman.
 */
public class HostPartyController implements Initializable {

  /** Contenedor de diseño vertical. */
  @FXML private VBox box;

  /** Campo de texto para ingresar el apodo del anfitrión. */
  @FXML private TextField nicknameField;

  /** ChoiceBox para seleccionar el tamaño de la sala de fiestas. */
  @FXML private ChoiceBox<Integer> roomSizeChoice;

  /**
   * Inicializa el controlador después de que se haya cargado la vista.
   * @param url La ubicación relativa del archivo FXML.
   * @param rb Recursos específicos del idioma.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // Cargar la hoja de estilo CSS para la vista de crear una fiesta
    URL cssURL = App.class.getResource("hostParty.css");
    String urlString = cssURL.toString();
    box.getStylesheets().add(urlString);

    // Configurar opciones predeterminadas para el tamaño de la sala de fiestas
    roomSizeChoice.getItems().addAll(2, 3, 4);
    roomSizeChoice.setValue(2); // Valor predeterminado
  }

  /**
   * Método que se ejecuta al intentar crear una fiesta.
   * Lee los datos ingresados y cambia la vista a la sala de fiestas del anfitrión.
   * @throws IOException si hay un error al cambiar la vista.
   */
  @FXML
  private void createParty() throws IOException {
    // Cambiar la vista a la sala de fiestas del anfitrión
    App.setRoot("hostLobbyRoom");

    // Obtener el controlador actual de la sala de fiestas del anfitrión
    Initializable controller = App.getCurrentController();

    // Verificar que el controlador sea del tipo HostLobbyController
    if (controller instanceof HostLobbyController) {
      // Establecer el apodo y el tamaño de la sala en el controlador de la sala de fiestas del anfitrión
      ((HostLobbyController)controller).setNickname(nicknameField.getText());
      ((HostLobbyController)controller).setRoomSize(roomSizeChoice.getValue());
    }
  }

  /**
   * Método que se ejecuta al querer cambiar a la vista del menú multijugador.
   * @throws IOException si hay un error al cambiar la vista.
   */
  @FXML
  private void switchToMultiplayer() throws IOException {
    // Cambiar la vista al menú multijugador
    App.setRoot("multiplayerMenu");
  }
}

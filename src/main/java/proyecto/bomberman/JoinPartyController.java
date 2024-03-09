package proyecto.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

/**
 * Controlador para la vista de unirse a una fiesta en el juego.
 */
public class JoinPartyController implements Initializable {

  /** Contenedor de diseño vertical. */
  @FXML private VBox box;

  /** Campo de texto para ingresar el apodo del jugador. */
  @FXML private TextField nicknameField;

  /** Campo de texto para ingresar el puerto del servidor. */
  @FXML private TextField portField;

  /**
   * Inicializa el controlador después de que se haya cargado la vista.
   * @param url La ubicación relativa del archivo FXML.
   * @param rb Recursos específicos del idioma.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // Cargar la hoja de estilo CSS para la vista de unirse a la fiesta
    URL cssURL = App.class.getResource("hostParty.css");
    String urlString = cssURL.toString();
    box.getStylesheets().add(urlString);
  }

  /**
   * Método que se ejecuta al intentar unirse a una fiesta.
   * Lee los datos ingresados y cambia la vista al lobby del juego.
   * @throws IOException si hay un error al cambiar la vista.
   */
  @FXML
  private void joinParty() throws IOException {
    String nickname = nicknameField.getText().trim();
    String port = portField.getText().trim();

    // Verificar que los campos no estén vacíos
    if (nickname.isEmpty() || port.isEmpty()) {
      return;
    }

/**
 * Intenta convertir el puerto proporcionado a un entero, y luego cambia la vista al lobby del juego.
 * Obtiene el controlador actual del lobby y establece el apodo y el puerto en él si es del tipo LobbyController.
 * 
 * @param port el puerto proporcionado como una cadena
 * @param nickname el apodo del usuario
 */
try {
  Integer portNumber = Integer.parseInt(port); // Convertir el puerto a entero
  App.setRoot("lobbyRoom"); // Cambiar la vista al lobby del juego

  // Obtener el controlador actual del lobby
  Initializable controller = App.getCurrentController();

  // Verificar que el controlador sea del tipo LobbyController
  if (controller instanceof LobbyController) {
      // Establecer el apodo y el puerto en el controlador del lobby
      ((LobbyController) controller).setNickname(nickname);
      ((LobbyController) controller).setPort(portNumber);
  }
} catch (NumberFormatException e) {
  // Manejar la excepción si el puerto no es un número válido
  System.err.println("El puerto no es un número válido.");
  e.printStackTrace();
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

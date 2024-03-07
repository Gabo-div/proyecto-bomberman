package proyecto.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.layout.*;

/**
 * Controlador para el menú multijugador del juego.
 */
public class MultiplayerMenuController implements Initializable {

  /** Contenedor de diseño vertical. */
  @FXML private VBox box;

  /**
   * Inicializa el controlador después de que se haya cargado la vista.
   * @param url La ubicación relativa del archivo FXML.
   * @param rb  Recursos específicos del idioma.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // Cargar la hoja de estilo CSS para el menú multijugador
    URL cssURL = App.class.getResource("multiplayerMenu.css");
    String urlString = cssURL.toString();
    box.getStylesheets().add(urlString);
  }

  /**
   * Cambia a la vista para hostear una partida.
   * @throws IOException Si ocurre un error al cargar la vista para hostear una partida.
   */
  @FXML
  private void switchToHostParty() throws IOException {
    App.setRoot("hostParty");
  }

  /**
   * Cambia a la vista de unirse a un lobby.
   * @throws IOException Si ocurre un error al cargar la vista de unirse a un lobby.
   */
  @FXML
  private void switchToJoinLobby() throws IOException {
    App.setRoot("joinParty");
  }

  /**
   * Cambia a la vista principal.
   * @throws IOException Si ocurre un error al cargar la vista principal.
   */
  @FXML
  private void switchToPrimary() throws IOException {
    App.setRoot("primary");
  }
}

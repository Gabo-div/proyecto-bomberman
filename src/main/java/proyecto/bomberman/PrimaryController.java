package proyecto.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.layout.*;

/**
 * Controlador para la vista principal del juego Bomberman.
 */
public class PrimaryController implements Initializable {

  /** Contenedor de diseño vertical principal. */
  @FXML private VBox box;

  /** Contenedor de diseño vertical secundario. */
  @FXML private VBox column;

  /**
   * Inicializa el controlador después de que se haya cargado la vista.
   * @param url La ubicación relativa del archivo FXML.
   * @param rb  Recursos específicos del idioma.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // Cargar la hoja de estilo CSS para la vista principal
    URL cssURL = App.class.getResource("primary.css");
    String urlString = cssURL.toString();
    box.getStylesheets().add(urlString);
  }

  /**
   * Cambia a la vista de un solo jugador.
   * @throws IOException Si ocurre un error al cargar la vista de jugador único.
   */
  @FXML
  private void switchToSinglePlayer() throws IOException {
    App.setRoot("singleplayer");
  }

  /**
   * Cambia a la vista de menú multijugador.
   * @throws IOException Si ocurre un error al cargar la vista de menú multijugador.
   */
  @FXML
  private void switchToMultiplayer() throws IOException {
    App.setRoot("multiplayerMenu");
  }

  /**
   * Cambia a la vista de ayuda.
   * @throws IOException Si ocurre un error al cargar la vista de ayuda.
   */
  @FXML
  private void switchToHelp() throws IOException {
    App.setRoot("help");
  }

  /**
   * Cambia a la vista "Acerca de".
   * @throws IOException Si ocurre un error al cargar la vista "Acerca de".
   */
  @FXML
  private void switchToAbout() throws IOException {
    App.setRoot("about");
  }

  /**
   * Cierra la aplicación.
   */
  @FXML
  public void Exit() {
    App.getStage().close();
    System.exit(0);
  }
}

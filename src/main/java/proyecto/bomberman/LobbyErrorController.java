package proyecto.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.layout.*;

/**
 * Controlador para la vista de error del lobby en el juego.
 */
public class LobbyErrorController implements Initializable {

  /** Contenedor de diseño vertical principal. */
  @FXML private VBox box;

  /** Contenedor de diseño vertical secundario. */
  @FXML private VBox content;

  /**
   * Inicializa el controlador después de que se haya cargado la vista.
   * @param url La ubicación relativa del archivo FXML.
   * @param rb  Recursos específicos del idioma.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // Cargar la hoja de estilo CSS para la vista de error del lobby
    URL cssURL = App.class.getResource("lobbyError.css");
    String urlString = cssURL.toString();
    box.getStylesheets().add(urlString);

    // Enlazar la altura del contenido con la altura de la ventana de la aplicación
    content.prefHeightProperty().bind(App.getStage().heightProperty());
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

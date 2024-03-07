package proyecto.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.layout.*;

/**
 * Controlador para la vista de ayuda en el juego.
 */
public class HelpController implements Initializable {
    /** Contenedor de diseño vertical. */
    @FXML private VBox box;
  
    /**
     * Inicializa el controlador después de que se haya cargado la vista.
     * @param url La ubicación relativa del archivo FXML.
     * @param rb Recursos específicos del idioma.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      // Cargar la hoja de estilo CSS para la vista de ayuda
      URL cssURL = App.class.getResource("help.css");
      String urlString = cssURL.toString();
      box.getStylesheets().add(urlString);
    }

    /**
     * Cambia a la vista principal.
     * @throws IOException si hay un error al cambiar la vista.
     */
    @FXML
    private void switchToPrimary() throws IOException {
      App.setRoot("primary");
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

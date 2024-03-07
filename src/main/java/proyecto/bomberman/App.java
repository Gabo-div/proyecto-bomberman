package proyecto.bomberman;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Clase principal del juego.
 */
public class App extends Application {

  /** Contenedor de diseño vertical. */
  @FXML private VBox box;
  /** Escena actual del juego. */
  private static Scene scene;
  /** Controlador actual cargado en la escena. */
  private static Initializable currentController;

  /**
   * Método de inicio de la aplicación.
   * @param stage El escenario principal del juego.
   * @throws IOException Si hay un error al cargar la escena inicial.
   */
  @Override
  public void start(Stage stage) throws IOException {

    scene = new Scene(loadFXML("primary"), 1280, 720);
    Image icon =
        new Image(getClass().getResourceAsStream("/Bomberman-icon.png"));

    stage.setTitle("Don Pepe and His Balloons");
    stage.getIcons().add(icon);
    stage.setScene(scene);
    stage.setResizable(true);
    stage.show();

    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
      @Override
      public void handle(WindowEvent t) {
        Platform.exit();
        System.exit(0);
      }
    });
  }

  /**
   * Establece la vista raíz del juego.
   * @param fxml El archivo FXML de la vista que se desea cargar.
   * @throws IOException Si hay un error al cargar el archivo FXML.
   */
  static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFXML(fxml));
  }

  /**
   * Carga un archivo FXML y devuelve el nodo raíz de la jerarquía.
   * @param fxml El archivo FXML que se desea cargar.
   * @return El nodo raíz del árbol de nodos cargado desde el archivo FXML.
   * @throws IOException Si hay un error al cargar el archivo FXML.
   */
  private static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader =
        new FXMLLoader(App.class.getResource(fxml + ".fxml"));

    Parent root = fxmlLoader.load();
    currentController = fxmlLoader.getController();

    return root;
  }

  /**
   * Método principal del juego.
   * @param args Los argumentos de la línea de comandos.
   */
  public static void main(String[] args) { launch(); }

  /**
   * Obtiene la escena actual del juego.
   * @return La escena actual del juego.
   */
  public static Scene getScene() { return scene; }

  /**
   * Obtiene el escenario principal del juego.
   * @return El escenario principal del juego.
   */
  public static Stage getStage() { return (Stage)scene.getWindow(); }

  /**
   * Obtiene el controlador actual cargado en la escena.
   * @return El controlador actual cargado en la escena.
   */
  public static Initializable getCurrentController() {
    return currentController;
  }
}

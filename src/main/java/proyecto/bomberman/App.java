package proyecto.bomberman;

import Multiplayer.Client;
import Multiplayer.Server;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * JavaFX App
 *
 */

public class App extends Application {

  private static Scene scene;

  @Override
  public void start(Stage stage) throws IOException {

    scene = new Scene(loadFXML("primary"), 800, 600, Color.AQUAMARINE);
    Image icon =
        new Image(getClass().getResourceAsStream("/Bomberman-icon.png"));
    stage.setTitle("Don Pepe and His Balloons");
    stage.getIcons().add(icon);
    stage.setScene(scene);

    stage.setResizable(true);
    stage.show();
  }

  static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFXML(fxml));
  }

  private static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader =
        new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    return fxmlLoader.load();
  }

  public static void main(String[] args) { launch(); }

  public static Scene getScene() { return scene; }
}

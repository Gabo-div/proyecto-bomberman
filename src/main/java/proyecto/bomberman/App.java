package proyecto.bomberman;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

  @FXML private VBox box;
  private static Scene scene;
  private static Initializable currentController;

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
  }

  static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFXML(fxml));
  }

  private static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader =
        new FXMLLoader(App.class.getResource(fxml + ".fxml"));

    Parent root = fxmlLoader.load();
    currentController = fxmlLoader.getController();

    return root;
  }

  public static void main(String[] args) { launch(); }

  public static Scene getScene() { return scene; }

  public static Stage getStage() { return (Stage)scene.getWindow(); }

  public static Initializable getCurrentController() {
    return currentController;
  }
}

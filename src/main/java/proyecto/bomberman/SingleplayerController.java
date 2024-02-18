package proyecto.bomberman;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.layout.*;
import proyecto.game.*;

public class SingleplayerController implements Initializable {
  @FXML private VBox box;

  private Canvas canvas;
  private SingleplayerGame game = SingleplayerGame.getInstance();
  private Scene scene = App.getScene();
  private Renderer renderer;

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    URL cssURL = App.class.getResource("singleplayer.css");
    String urlString = cssURL.toString();
    box.getStylesheets().add(urlString);

    renderer = new Renderer(canvas, box);
    renderer.resizeCanvas();
    ChangeListener<Number> sizeListener = (observable, oldValue, newValue) -> {
      renderer.resizeCanvas();
    };

    scene.widthProperty().addListener(sizeListener);
    scene.heightProperty().addListener(sizeListener);

    KeyHandler.getInstance().pollScene(scene);
    game.start();
    initTimer();
  }

  private void initTimer() {
    new GameTimer() {
      @Override
      public void tick(double deltaMs) {
        if (!game.isRunning()) {
          return;
        }
        game.loop(deltaMs);
        renderer.render();
      }
    }.start();
  }
}

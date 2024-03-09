package proyecto.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import proyecto.game.*;

public class SingleplayerController implements Initializable {
  @FXML private VBox box;

  private Canvas canvas;
  private SingleplayerGame game = SingleplayerGame.getInstance();
  private Scene scene = App.getScene();
  private Renderer renderer;

  private KeyHandler keyHandler = KeyHandler.getInstance();

  private GameTimer gameTimer = new GameTimer() {
    @Override
    public void tick(double deltaMs) {
      if (game.getGameState() != GameState.RUNNING) {
        gameTimer.stop();
      }

      game.loop(deltaMs);
      renderer.render();
    }
  };

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    URL cssURL = App.class.getResource("singleplayer.css");
    String urlString = cssURL.toString();
    box.getStylesheets().add(urlString);

    renderer = new Renderer(canvas, box);

    ChangeListener<Number> sizeListener = (observable, oldValue, newValue) -> {
      renderer.resizeCanvas(scene.getWidth(), scene.getHeight());
    };

    scene.widthProperty().addListener(sizeListener);
    scene.heightProperty().addListener(sizeListener);
    sizeListener.changed(null, null, null);

    keyHandler.pollScene(scene);

    keyHandler.onPressed(KeyCode.ESCAPE, () -> {
      GameState gameState = game.getGameState();

      if (gameState == GameState.GAMEOVER || gameState == GameState.WIN) {
        try {
          gameTimer.stop();
          game.end();
          App.setRoot("primary");
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });

    keyHandler.onPressed(KeyCode.ESCAPE, () -> {
      GameState gameState = game.getGameState();

      if (gameState == GameState.RUNNING) {
        game.setGameState(GameState.PAUSED);
      } else if (gameState == GameState.PAUSED) {
        game.setGameState(GameState.RUNNING);
        gameTimer.start();
      }
    });

    game.start();
    gameTimer.start();
  }
}

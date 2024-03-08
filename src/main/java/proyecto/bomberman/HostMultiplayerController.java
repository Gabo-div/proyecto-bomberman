
package proyecto.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import proyecto.game.GameState;
import proyecto.game.GameTimer;
import proyecto.game.KeyHandler;
import proyecto.game.MultiplayerGame;
import proyecto.game.MultiplayerRenderer;
import proyecto.model.Player;
import proyecto.multiplayer.GameServer;
import proyecto.multiplayer.ServerState;
import proyecto.multiplayer.User;

public class HostMultiplayerController implements Initializable {
  @FXML private VBox box;
  private Canvas canvas;

  private GameServer server = GameServer.getInstance();
  private MultiplayerGame game = MultiplayerGame.getInstance();
  private MultiplayerRenderer renderer;

  private Scene scene = App.getScene();

  private KeyHandler keyHandler = KeyHandler.getInstance();

  private GameTimer gameTimer = new GameTimer() {
    @Override
    public void tick(double deltaMs) {
      if (game.getGameState() != GameState.RUNNING) {
        gameTimer.stop();
      }

      Integer movementStateX = 0;
      Integer movementStateY = 0;

      if (keyHandler.isDown(KeyCode.A)) {
        movementStateX -= 1;
      }
      if (keyHandler.isDown(KeyCode.D)) {
        movementStateX += 1;
      }
      if (keyHandler.isDown(KeyCode.W)) {
        movementStateY -= 1;
      }
      if (keyHandler.isDown(KeyCode.S)) {
        movementStateY += 1;
      }

      User host = server.getHost();
      Player hostPlayer = game.getPlayer(host.getName());
      hostPlayer.setMovementStateX(movementStateX);
      hostPlayer.setMovementStateY(movementStateY);

      game.loop(deltaMs);
      renderer.render();
      server.syncClients();
    }
  };

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    URL cssURL = App.class.getResource("hostMultiplayer.css");
    String urlString = cssURL.toString();
    box.getStylesheets().add(urlString);

    if (server.getState() != ServerState.INGAME) {
      try {
        server.stop();
        App.setRoot("lobbyError");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    game.start(server.getUsers());

    renderer = new MultiplayerRenderer(canvas, box);

    ChangeListener<Number> sizeListener = (observable, oldValue, newValue) -> {
      renderer.resizeCanvas(scene.getWidth(), scene.getHeight());
    };

    scene.widthProperty().addListener(sizeListener);
    scene.heightProperty().addListener(sizeListener);
    sizeListener.changed(null, null, null);

    keyHandler.pollScene(scene);

    keyHandler.onPressed(KeyCode.ENTER, () -> {
      if (game.getGameState() == GameState.RUNNING) {
        User host = server.getHost();
        game.addBomb(host.getName());
      }
    });

    gameTimer.start();
  }
}

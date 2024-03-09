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

/**
 * Controlador para la vista de juego multijugador en modo anfitrión.
 */
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
      // Detiene el temporizador si el juego no está en ejecución
      if (game.getGameState() != GameState.RUNNING) {
        gameTimer.stop();
      }

      // Determina el estado de movimiento basado en las teclas presionadas
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

      // Obtiene el usuario anfitrión y actualiza su estado de movimiento
      User host = server.getHost();
      Player hostPlayer = game.getPlayer(host.getName());
      hostPlayer.setMovementStateX(movementStateX);
      hostPlayer.setMovementStateY(movementStateY);

      // Actualiza el estado del juego, renderiza y sincroniza con los clientes
      game.loop(deltaMs);
      renderer.render();
      server.syncClients();
    }
  };

  /**
   * Inicializa el controlador después de que se haya cargado la vista.
   * @param url La ubicación relativa del archivo FXML.
   * @param rb Recursos específicos del idioma.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // Carga el archivo CSS para estilizar la vista
    URL cssURL = App.class.getResource("hostMultiplayer.css");
    String urlString = cssURL.toString();
    box.getStylesheets().add(urlString);

    // Si el servidor no está en el estado INGAME, redirige a una vista de error
    if (server.getState() != ServerState.INGAME) {
      try {
        server.stop();
        App.setRoot("lobbyError");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // Inicia el juego con los usuarios conectados al servidor
    game.start(server.getUsers());

    // Inicia el renderizador
    renderer = new MultiplayerRenderer(canvas, box);

    // Ajusta el tamaño del canvas al cambiar el tamaño de la ventana
    ChangeListener<Number> sizeListener = (observable, oldValue, newValue) -> {
      renderer.resizeCanvas(scene.getWidth(), scene.getHeight());
    };
    scene.widthProperty().addListener(sizeListener);
    scene.heightProperty().addListener(sizeListener);
    sizeListener.changed(null, null, null);

    // Escucha las teclas presionadas para el movimiento y la acción de soltar bomba
    keyHandler.pollScene(scene);
    keyHandler.onPressed(KeyCode.ENTER, () -> {
      if (game.getGameState() == GameState.RUNNING) {
        User host = server.getHost();
        game.addBomb(host.getName());
      }
    });

    // Escucha la tecla ESCAPE para volver a la sala de espera al finalizar el juego
    keyHandler.onPressed(KeyCode.ESCAPE, () -> {
      if (game.getGameState() == GameState.END) {
        try {
          server.endGame();
          App.setRoot("hostLobbyRoom");
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });

    // Inicia el temporizador del juego
    gameTimer.start();
  }
}

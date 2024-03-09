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
import proyecto.multiplayer.GameClient;
import proyecto.multiplayer.ServerState;

/**
 * Controlador para la vista de juego multijugador.
 */
public class MultiplayerController implements Initializable {
  @FXML private VBox box;
  private Canvas canvas;

  private GameClient client = GameClient.getInstance();
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

      // Envía los datos de movimiento al servidor y renderiza la escena
      client.sendMovement(movementStateX, movementStateY);
      renderer.render();
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
    URL cssURL = App.class.getResource("multiplayer.css");
    String urlString = cssURL.toString();
    box.getStylesheets().add(urlString);

    // Si el cliente no está en el estado INGAME, redirige a una vista de error
    if (client.getState() != ServerState.INGAME) {
      try {
        client.stop();
        App.setRoot("lobbyError");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // Inicia el juego y el renderizador
    game.start();
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
      if (client.getState() == ServerState.INGAME) {
        client.sendBomb();
      }
    });

    // Escucha los cambios de estado del servidor y maneja la transición a la sala de espera
    client.setOnStateChange((state) -> {
      if (state == ServerState.CONNECTED) {
        try {
          game.end();
          App.setRoot("lobbyRoom");
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });

    // Inicia el temporizador del juego
    gameTimer.start();
  }
}
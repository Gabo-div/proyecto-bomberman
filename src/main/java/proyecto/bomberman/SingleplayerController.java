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

/**
 * Controlador para la vista de un solo jugador en el juego.
 */
public class SingleplayerController implements Initializable {

  /** Contenedor de diseño vertical. */
  @FXML private VBox box;

  /** Objeto Canvas para el renderizado del juego. */
  private Canvas canvas;

  /** Instancia del juego para un jugador. */
  private SingleplayerGame game = SingleplayerGame.getInstance();

  /** Escena de la aplicación. */
  private Scene scene = App.getScene();

  /** Renderizador del juego. */
  private Renderer renderer;

  /** Manejador de teclas. */
  private KeyHandler keyHandler = KeyHandler.getInstance();

  /** Temporizador del juego. */
  private GameTimer gameTimer = new GameTimer() {
    @Override
    public void tick(double deltaMs) {
      // Detener el temporizador si el estado del juego no es "EJECUTÁNDOSE"
      if (game.getGameState() != GameState.RUNNING) {
        gameTimer.stop();
      }

      // Ejecutar un ciclo de juego y renderizar
      game.loop(deltaMs);
      renderer.render();
    }
  };

  /**
   * Inicializa el controlador después de que se haya cargado la vista.
   * @param url La ubicación relativa del archivo FXML.
   * @param rb  Recursos específicos del idioma.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {

    // Cargar la hoja de estilo CSS para el diseño de un solo jugador 
    URL cssURL = App.class.getResource("singleplayer.css");
    String urlString = cssURL.toString();
    box.getStylesheets().add(urlString);

    // Inicializar el renderizador del juego
    renderer = new Renderer(canvas, box);

    // Ajustar el tamaño del lienzo según el tamaño de la escena
    ChangeListener<Number> sizeListener = (observable, oldValue, newValue) -> {
      renderer.resizeCanvas(scene.getWidth(), scene.getHeight());
    };

    scene.widthProperty().addListener(sizeListener);
    scene.heightProperty().addListener(sizeListener);
    sizeListener.changed(null, null, null);

    // Configurar el manejo de teclas para la escena
    keyHandler.pollScene(scene);

    // Configurar el comportamiento del botón de escape
    keyHandler.onPressed(KeyCode.ESCAPE, () -> {
      GameState gameState = game.getGameState();

      // Si el juego ha terminado, volver a la vista principal
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

    // Cambiar entre pausa y juego al presionar el botón de escape
    keyHandler.onPressed(KeyCode.ESCAPE, () -> {
      GameState gameState = game.getGameState();

      if (gameState == GameState.RUNNING) {
        game.setGameState(GameState.PAUSED);
      } else if (gameState == GameState.PAUSED) {
        game.setGameState(GameState.RUNNING);
        gameTimer.start();
      }
    });

    // Iniciar el juego y el temporizador del juego
    game.start();
    gameTimer.start();
  }
}

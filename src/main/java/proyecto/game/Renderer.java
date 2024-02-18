package proyecto.game;

import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import proyecto.bomberman.App;
import proyecto.model.Bomb;
import proyecto.model.Coord;
import proyecto.model.Level;
import proyecto.model.Sprite;
import proyecto.model.SpriteSheet;

public class Renderer {

  private Scene scene = App.getScene();
  private VBox box;

  private Canvas mainCanvas;
  private Canvas offCanvas;

  private double blockSize;
  private double canvasHeight;
  private double canvasWidth;

  private GraphicsContext mainGc;
  private GraphicsContext offGc;

  private SingleplayerGame game = SingleplayerGame.getInstance();
  private SpriteSheet spriteSheet = game.getSpriteSheet();

  public Renderer(Canvas canvas, VBox box) {
    this.mainCanvas = canvas;
    this.box = box;
  }

  public void resizeCanvas() {
    double sceneHeight = scene.getHeight();
    double sceneWidth = scene.getWidth();
    Level level = game.getLevel();

    if (sceneHeight < sceneWidth) {
      canvasHeight = sceneHeight;
      blockSize = canvasHeight / level.getHeight();
      canvasWidth = level.getWidth() * blockSize;
    } else {
      canvasWidth = sceneWidth;
      blockSize = canvasWidth / level.getWidth();
      canvasHeight = level.getHeight() * blockSize;
    }

    if (mainCanvas == null || offCanvas == null) {
      mainCanvas = new Canvas(canvasWidth, canvasHeight);
      mainGc = mainCanvas.getGraphicsContext2D();

      offCanvas = new Canvas(canvasWidth, canvasHeight);
      offGc = offCanvas.getGraphicsContext2D();

      box.getChildren().add(mainCanvas);
    } else {
      mainCanvas.setWidth(canvasWidth);
      mainCanvas.setHeight(canvasHeight);
      offCanvas.setWidth(canvasWidth);
      offCanvas.setHeight(canvasHeight);
    }

    offGc.scale(blockSize, blockSize);
  }

  public void render() {
    drawOff();
    drawMain();
  }

  private void drawOff() {
    drawBlocks();
    drawBombs();
    drawPlayer();
  }

  private void drawBlocks() {
    Sprite blockSprite = spriteSheet.getSprite("block");
    Sprite brickSprite = spriteSheet.getSprite("brick");

    Level level = game.getLevel();

    offGc.setFill(Color.web("#00ff48"));
    offGc.fillRect(0, 0, canvasWidth, canvasHeight);

    for (int y = 0; y < level.getHeight(); y++) {
      for (int x = 0; x < level.getWidth(); x++) {
        if (level.getTile(x, y) == 1) {
          offGc.drawImage(blockSprite.getImage(), x, y, 1, 1);
        }

        if (level.getTile(x, y) == 2) {
          offGc.drawImage(brickSprite.getImage(), x, y, 1, 1);
        }
      }
    }
  }

  private void drawBombs() {
    Level level = game.getLevel();
    ArrayList<Bomb> bombs = game.getBombs();

    for (int i = 0; i < bombs.size(); i++) {
      Bomb bomb = bombs.get(i);
      Coord<Integer> bombCoord = bomb.getCoord();

      offGc.setFill(Color.RED);
      offGc.fillRect(bombCoord.x, bombCoord.y, 1, 1);

      if (!bomb.exploded()) {
        continue;
      }

      int radius = bomb.getRadius();

      offGc.setFill(Color.RED);

      for (int x = 0; x <= radius; x++) {

        if (bombCoord.x + x >= level.getWidth()) {
          continue;
        }

        if (level.getTile(bombCoord.x + x, bombCoord.y) == 1) {
          break;
        }

        offGc.fillRect(bombCoord.x + x, bombCoord.y, 1, 1);
      }

      for (int x = 0; x >= -radius; x--) {

        if (bombCoord.x + x < 0) {
          continue;
        }

        if (level.getTile(bombCoord.x + x, bombCoord.y) == 1) {
          break;
        }

        offGc.fillRect(bombCoord.x + x, bombCoord.y, 1, 1);
      }

      for (int y = 0; y <= radius; y++) {

        if (bombCoord.y + y >= level.getHeight()) {
          continue;
        }

        if (level.getTile(bombCoord.x, bombCoord.y + y) == 1) {
          break;
        }

        offGc.fillRect(bombCoord.x, bombCoord.y + y, 1, 1);
      }

      for (int y = 0; y >= -radius; y--) {

        if (bombCoord.y + y < 0) {
          continue;
        }

        if (level.getTile(bombCoord.x, bombCoord.y + y) == 1) {
          break;
        }

        offGc.fillRect(bombCoord.x, bombCoord.y + y, 1, 1);
      }
    }
  }

  private void drawPlayer() {
    Coord<Double> playerCoord = game.getPlayer().getCoord();

    offGc.setFill(Color.BLUE);
    offGc.fillRect(playerCoord.x, playerCoord.y, 1, 1);
  }

  private void drawMain() {
    Image offScreenImage = offCanvas.snapshot(null, null);
    mainGc.drawImage(offScreenImage, 0, 0);
  }
}

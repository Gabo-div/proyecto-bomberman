package proyecto.game;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import proyecto.model.AirBlock;
import proyecto.model.Block;
import proyecto.model.Bomb;
import proyecto.model.BrickBlock;
import proyecto.model.Character;
import proyecto.model.Coord;
import proyecto.model.Enemy;
import proyecto.model.Level;
import proyecto.model.Sprite;
import proyecto.model.SpriteSheet;
import proyecto.model.WallBlock;

public class Renderer {
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

  private Affine defaultTransform;

  public Renderer(Canvas canvas, VBox box) {
    this.mainCanvas = canvas;
    this.box = box;
  }

  public void resizeCanvas(double sceneWidth, double sceneHeight) {
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

      defaultTransform = offGc.getTransform();

      box.getChildren().add(mainCanvas);
    } else {
      mainCanvas.setWidth(canvasWidth);
      mainCanvas.setHeight(canvasHeight);
      offCanvas.setWidth(canvasWidth);
      offCanvas.setHeight(canvasHeight);

      offGc.setTransform(defaultTransform);
    }

    offGc.scale(blockSize, blockSize);
  }

  public void render() {
    drawOff();
    drawMain();
  }

  private void drawOff() {
    offGc.clearRect(0, 0, canvasWidth, canvasHeight);
    offGc.setFill(Color.web("#00ff48"));
    offGc.fillRect(0, 0, canvasWidth, canvasHeight);

    drawBlocks();
    drawPlayer();
    drawEnemies();
    drawBombs();
    drawInfo();

    if (game.getGameState() == GameState.GAMEOVER) {
      drawGameOver();
    }

    if (game.getGameState() == GameState.PAUSED) {
      drawPaused();
    }
  }

  private void drawBlocks() {
    Sprite blockSprite = spriteSheet.getSprite("block");
    Sprite brickSprite = spriteSheet.getSprite("brick");

    Level level = game.getLevel();

    for (int y = 0; y < level.getHeight(); y++) {
      for (int x = 0; x < level.getWidth(); x++) {

        Block block = level.getBlock(x, y);

        if (block instanceof WallBlock) {
          offGc.drawImage(blockSprite.getImage(), x, y, 1, 1);
        }

        if (block instanceof BrickBlock) {
          offGc.drawImage(brickSprite.getImage(), x, y, 1, 1);
        }
      }
    }
  }

  private void drawBombs() {
    Level level = game.getLevel();
    ArrayList<Bomb> bombs = game.getBombs();

    BiFunction<Integer, Integer, Boolean> explosion =
        (Integer x, Integer y) -> {
      Coord<Integer> eCoord = new Coord<>(x, y);

      if (eCoord.x >= level.getWidth() || eCoord.y >= level.getHeight() ||
          eCoord.y < 0 || eCoord.x < 0) {
        return true;
      }

      if (level.getBlock(eCoord) instanceof WallBlock) {
        return false;
      }

      if (level.getBlock(eCoord) instanceof AirBlock) {
        offGc.setFill(Color.ORANGE);
        offGc.fillRect(eCoord.x, eCoord.y, 1, 1);

        return true;
      }

      return true;
    };

    for (int i = 0; i < bombs.size(); i++) {
      Bomb bomb = bombs.get(i);
      Coord<Integer> bombCoord = bomb.getCoord();
      int radius = bomb.getRadius();

      if (!bomb.exploded()) {
        offGc.setFill(Color.RED);
        offGc.fillRect(bombCoord.x, bombCoord.y, 1, 1);
        continue;
      }

      offGc.setFill(Color.ORANGE);
      offGc.fillRect(bombCoord.x, bombCoord.y, 1, 1);

      for (int x = 1; x <= radius; x++) {
        if (!explosion.apply(bombCoord.x + x, bombCoord.y)) {
          break;
        }
      }

      for (int x = -1; x >= -radius; x--) {
        if (!explosion.apply(bombCoord.x + x, bombCoord.y)) {
          break;
        }
      }

      for (int y = 1; y <= radius; y++) {
        if (!explosion.apply(bombCoord.x, bombCoord.y + y)) {
          break;
        }
      }

      for (int y = -1; y >= -radius; y--) {
        if (!explosion.apply(bombCoord.x, bombCoord.y + y)) {
          break;
        }
      }
    }
  }

  private void drawPlayer() {
    Coord<Double> playerCoord = game.getPlayer().getCoord();
    Boolean invincible = game.getPlayer().isInvincible();

    if (invincible) {
      offGc.setGlobalAlpha(0.5);
    }
    offGc.setFill(Color.BLUE);
    offGc.fillRect(playerCoord.x, playerCoord.y, 1, 1);

    offGc.setGlobalAlpha(1);
  }

  private void drawEnemies() {
    Level level = game.getLevel();
    List<Character> characters = level.getCharacters();

    for (Character character : characters) {
      if (!(character instanceof Enemy)) {
        continue;
      }

      if (character.isDead()) {
        continue;
      }

      Coord<Double> coord = character.getCoord();
      offGc.setFill(Color.VIOLET);
      offGc.fillRect(coord.x, coord.y, 1, 1);
    }
  }

  private void drawInfo() {
    offGc.setTransform(defaultTransform);

    double infoHeight = blockSize / 1.25;
    offGc.setFill(Color.ORANGE);
    offGc.fillRect(0, 0, canvasWidth, infoHeight);

    // Stroke
    offGc.setFill(Color.BLACK);
    offGc.fillRect(0, blockSize / 1.25 - 1, canvasWidth, 1);

    // Info
    double fontSize = infoHeight / 1.25;
    double rectHeight = fontSize + 5;
    double rectWidth = fontSize;
    double rectY = (infoHeight - rectHeight) / 2;
    double rectX = blockSize;
    offGc.fillRect(rectX, rectY, rectWidth, rectHeight);

    // Lifes
    double lifeX = rectX + (rectWidth / 2);
    double lifeY = rectY + (rectHeight / 2) + (fontSize / 3);
    Integer lifes = game.getPlayer().getLifes();

    offGc.setFill(Color.WHITE);
    offGc.setFont(new Font("Arial", fontSize));
    offGc.setTextAlign(TextAlignment.CENTER);
    offGc.fillText(lifes.toString(), lifeX, lifeY);

    offGc.scale(blockSize, blockSize);
  }

  private void drawGameOver() {

    offGc.setTransform(defaultTransform);

    offGc.setGlobalAlpha(0.75);
    offGc.setFill(Color.web("#000000"));
    offGc.fillRect(0, 0, canvasWidth, canvasHeight);

    offGc.setGlobalAlpha(1);

    offGc.setFill(Color.WHITE);
    offGc.setTextAlign(TextAlignment.CENTER);

    offGc.setFont(new Font("Arial", blockSize));
    offGc.fillText("Game Over", canvasWidth / 2,
                   canvasHeight / 2 - blockSize / 3);

    offGc.setFont(new Font("Arial", blockSize / 3));
    offGc.fillText("Pulsa ESC para volver", canvasWidth / 2,
                   canvasHeight / 2 + blockSize / 3);

    offGc.scale(blockSize, blockSize);
  }

  private void drawPaused() {
    offGc.setTransform(defaultTransform);

    offGc.setGlobalAlpha(0.75);
    offGc.setFill(Color.web("#000000"));
    offGc.fillRect(0, 0, canvasWidth, canvasHeight);

    offGc.setGlobalAlpha(1);

    offGc.setFill(Color.WHITE);
    offGc.setTextAlign(TextAlignment.CENTER);

    offGc.setFont(new Font("Arial", blockSize));
    offGc.fillText("Pausa", canvasWidth / 2, canvasHeight / 2 - blockSize / 3);

    offGc.setFont(new Font("Arial", blockSize / 3));
    offGc.fillText("Pulsa ESC para reanudar", canvasWidth / 2,
                   canvasHeight / 2 + blockSize / 3);

    offGc.scale(blockSize, blockSize);
  }

  private void drawMain() {
    Image offScreenImage = offCanvas.snapshot(null, null);
    mainGc.drawImage(offScreenImage, 0, 0);
  }
}

package proyecto.game;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import proyecto.model.AirBlock;
import proyecto.model.Block;
import proyecto.model.Bomb;
import proyecto.model.BombUp;
import proyecto.model.BrickBlock;
import proyecto.model.Coord;
import proyecto.model.Direction;
import proyecto.model.Entity;
import proyecto.model.FireUp;
import proyecto.model.Level;
import proyecto.model.LifeUp;
import proyecto.model.Player;
import proyecto.model.PowerUp;
import proyecto.model.Sprite;
import proyecto.model.SpriteSheet;
import proyecto.model.WallBlock;
import proyecto.multiplayer.CharacterColor;

public class MultiplayerRenderer {
  private VBox box;

  private Canvas mainCanvas;
  private Canvas offCanvas;

  private double blockSize;
  private double canvasHeight;
  private double canvasWidth;
  private int gap = 2;

  private GraphicsContext mainGc;
  private GraphicsContext offGc;

  private MultiplayerGame game = MultiplayerGame.getInstance();
  private SpriteSheet spriteSheet = SpriteSheet.getInstance();

  private Affine defaultTransform;

  public MultiplayerRenderer(Canvas canvas, VBox box) {
    this.mainCanvas = canvas;
    this.box = box;
  }

  public void resizeCanvas(double sceneWidth, double sceneHeight) {
    Level level = game.getLevel();

    if (sceneHeight < sceneWidth) {
      canvasHeight = sceneHeight;
      blockSize = canvasHeight / (level.getHeight() + gap);
      canvasWidth = (level.getWidth() + gap) * blockSize;
    } else {
      canvasWidth = sceneWidth;
      blockSize = canvasWidth / (level.getWidth() + gap);
      canvasHeight = (level.getHeight() + gap) * blockSize;
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

    offGc.setTransform(defaultTransform);
    offGc.scale(blockSize, blockSize);
  }

  public void render() {
    if (game.getGameState() == GameState.NONE) {
      return;
    }

    drawOff();
    drawMain();
  }

  private void drawOff() {
    offGc.clearRect(0, 0, canvasWidth, canvasHeight);
    offGc.setImageSmoothing(false);

    offGc.save();
    offGc.translate(gap / 2, gap);
    drawBlocks();
    drawBombs();
    drawPlayers();

    offGc.restore();
    drawGap();
    drawInfo();

    if (game.getGameState() == GameState.PAUSED) {
      drawPaused();
    }

    if (game.getGameState() == GameState.END) {
      drawEnd();
    }
  }

  private void drawBlocks() {
    Sprite wallTopSprite = spriteSheet.getSprite("border_top");
    Sprite wallBottomSprite = spriteSheet.getSprite("border_bottom");
    Sprite wallLeftSprite = spriteSheet.getSprite("border_left_2");
    Sprite wallRightSprite = spriteSheet.getSprite("border_right_1");

    Sprite wallTopLeftSprite = spriteSheet.getSprite("border_top_left_2");
    Sprite wallTopRightSprite = spriteSheet.getSprite("border_top_right_1");
    Sprite wallBottomLeftSprite = spriteSheet.getSprite("border_bottom_left_2");
    Sprite wallBottomRightSprite =
        spriteSheet.getSprite("border_bottom_right_1");

    Sprite wallSprite = spriteSheet.getSprite("wall");
    Sprite brickSprite = spriteSheet.getSprite("brick");

    Sprite floorSprite = spriteSheet.getSprite("floor");
    Sprite floorShadowTopSprite = spriteSheet.getSprite("floor_shadow_top");

    Level level = game.getLevel();

    for (int y = 0; y < level.getHeight(); y++) {
      for (int x = 0; x < level.getWidth(); x++) {

        Block block = level.getBlock(x, y);
        Block topBlock = y == 0 ? null : level.getBlock(x, y - 1);

        if (topBlock instanceof WallBlock) {
          offGc.drawImage(floorShadowTopSprite.getImage(), x, y, 1, 1);
        } else {
          offGc.drawImage(floorSprite.getImage(), x, y, 1, 1);
        }

        if (block instanceof WallBlock) {

          if (y == 0 && x == 0) {
            offGc.drawImage(wallTopLeftSprite.getImage(), x, y, 1, 1);
            continue;
          }

          if (y == 0 && x == level.getWidth() - 1) {
            offGc.drawImage(wallTopRightSprite.getImage(), x, y, 1, 1);
            continue;
          }

          if (y == level.getHeight() - 1 && x == 0) {
            offGc.drawImage(wallBottomLeftSprite.getImage(), x, y, 1, 1);
            continue;
          }

          if (y == level.getHeight() - 1 && x == level.getWidth() - 1) {
            offGc.drawImage(wallBottomRightSprite.getImage(), x, y, 1, 1);
            continue;
          }

          if (y == 0) {
            offGc.drawImage(wallTopSprite.getImage(), x, y, 1, 1);
            continue;
          }

          if (y == level.getHeight() - 1) {
            offGc.drawImage(wallBottomSprite.getImage(), x, y, 1, 1);
            continue;
          }

          if (x == 0) {
            offGc.drawImage(wallLeftSprite.getImage(), x, y, 1, 1);
            continue;
          }

          if (x == level.getWidth() - 1) {
            offGc.drawImage(wallRightSprite.getImage(), x, y, 1, 1);
            continue;
          }

          offGc.drawImage(wallSprite.getImage(), x, y, 1, 1);
          continue;
        }

        if (block instanceof AirBlock) {
          AirBlock airBlock = (AirBlock)block;
          Entity<Integer> entity = airBlock.getEntity();

          if (entity instanceof PowerUp) {
            drawPowerUp((PowerUp)entity);
            continue;
          }
        }

        if (block instanceof BrickBlock) {
          offGc.drawImage(brickSprite.getImage(), x, y, 1, 1);
          continue;
        }
      }
    }
  }

  private void drawPowerUp(PowerUp powerUp) {
    Sprite fireUpSprite = spriteSheet.getSprite("fire_up");
    Sprite bombUpSprite = spriteSheet.getSprite("bomb_up");
    Sprite lifeUpSprite = spriteSheet.getSprite("life_up");

    Coord<Integer> coord = powerUp.getCoord();

    if (powerUp instanceof FireUp) {
      offGc.drawImage(fireUpSprite.getImage(), coord.x, coord.y, 1, 1);
    }

    if (powerUp instanceof LifeUp) {
      offGc.drawImage(lifeUpSprite.getImage(), coord.x, coord.y, 1, 1);
    }

    if (powerUp instanceof BombUp) {
      offGc.drawImage(bombUpSprite.getImage(), coord.x, coord.y, 1, 1);
    }
  }

  private void drawBombs() {
    Sprite bombSprite = spriteSheet.getSprite("bomb");
    Sprite explosionCenterSprite = spriteSheet.getSprite("explosion_center");
    Sprite explosionVerticalSprite =
        spriteSheet.getSprite("explosion_vertical");
    Sprite explosionHorizontalSprite =
        spriteSheet.getSprite("explosion_horizontal");
    Sprite explosionBorderTopSprite =
        spriteSheet.getSprite("explosion_border_top");
    Sprite explosionBorderBottomSprite =
        spriteSheet.getSprite("explosion_border_bottom");
    Sprite explosionBorderRightSprite =
        spriteSheet.getSprite("explosion_border_right");
    Sprite explosionBorderLeftSprite =
        spriteSheet.getSprite("explosion_border_left");

    Level level = game.getLevel();
    ArrayList<Bomb> bombs = game.getBombs();

    BiFunction<Coord<Integer>, Bomb, Boolean> explosion =
        (Coord<Integer> eCoord, Bomb bomb) -> {

      if (eCoord.x >= level.getWidth() || eCoord.y >= level.getHeight() ||
          eCoord.y < 0 || eCoord.x < 0) {
        return true;
      }

      if (level.getBlock(eCoord) instanceof WallBlock) {
        return false;
      }

      if (level.getBlock(eCoord) instanceof AirBlock) {
        Coord<Integer> bombCoord = bomb.getCoord();
        Integer radius = bomb.getRadius();

        Image explosionImage = null;

        if (eCoord.y == bombCoord.y - radius) {
          explosionImage = explosionBorderTopSprite.getImage();
        } else if (eCoord.y == bombCoord.y + radius) {
          explosionImage = explosionBorderBottomSprite.getImage();
        } else if (eCoord.x == bombCoord.x - radius) {
          explosionImage = explosionBorderLeftSprite.getImage();
        } else if (eCoord.x == bombCoord.x + radius) {
          explosionImage = explosionBorderRightSprite.getImage();
        } else if (eCoord.x == bombCoord.x) {
          explosionImage = explosionVerticalSprite.getImage();
        } else if (eCoord.y == bombCoord.y) {
          explosionImage = explosionHorizontalSprite.getImage();
        }

        offGc.drawImage(explosionImage, eCoord.x, eCoord.y, 1, 1);

        return true;
      }

      return true;
    };

    for (int i = 0; i < bombs.size(); i++) {
      Bomb bomb = bombs.get(i);
      int radius = bomb.getRadius();
      Coord<Integer> bombCoord = bomb.getCoord();

      if (!bomb.exploded()) {
        offGc.drawImage(bombSprite.getImage(), bombCoord.x, bombCoord.y, 1, 1);
        continue;
      }

      offGc.drawImage(explosionCenterSprite.getImage(), bombCoord.x,
                      bombCoord.y, 1, 1);

      for (int x = 1; x <= radius; x++) {
        Coord<Integer> eCoord = new Coord<>(bombCoord.x + x, bombCoord.y);
        if (!explosion.apply(eCoord, bomb)) {
          break;
        }
      }

      for (int x = -1; x >= -radius; x--) {
        Coord<Integer> eCoord = new Coord<>(bombCoord.x + x, bombCoord.y);
        if (!explosion.apply(eCoord, bomb)) {
          break;
        }
      }

      for (int y = 1; y <= radius; y++) {
        Coord<Integer> eCoord = new Coord<>(bombCoord.x, bombCoord.y + y);
        if (!explosion.apply(eCoord, bomb)) {
          break;
        }
      }

      for (int y = -1; y >= -radius; y--) {
        Coord<Integer> eCoord = new Coord<>(bombCoord.x, bombCoord.y + y);
        if (!explosion.apply(eCoord, bomb)) {
          break;
        }
      }
    }
  }

  private void drawPlayers() {
    for (Player player : game.getPlayers()) {
      if (player.isDead()) {
        continue;
      }

      Coord<Double> playerCoord = player.getCoord();
      Boolean invincible = player.isInvincible();
      Direction direction = player.getDirection();
      Integer directionState = player.getDirectionState();
      String directionStr = direction.toString().toLowerCase();

      CharacterColor color = player.getColor();
      String colorStr = color.toString().toLowerCase();
      String spriteName = colorStr + "_" + directionStr + "_" + directionState;
      Sprite playerSprite = spriteSheet.getSprite(spriteName);
      Image playerImage = playerSprite.getImage();

      String nametag = player.getName();
      double fontSize = 1;

      offGc.setFont(new Font("Bomberman", fontSize));
      offGc.setTextAlign(TextAlignment.CENTER);
      offGc.fillText(nametag, playerCoord.x + 0.5, playerCoord.y - 0.5);

      if (invincible) {
        offGc.setGlobalAlpha(0.5);
      }

      offGc.drawImage(playerImage, playerCoord.x, playerCoord.y - 0.5, 1, 1.5);

      offGc.setGlobalAlpha(1);
    }
  }

  private void drawGap() {
    Sprite borderLeftSprite = spriteSheet.getSprite("border_left_1");
    Sprite borderRightSprite = spriteSheet.getSprite("border_right_2");

    Sprite borderTopLeftSprite = spriteSheet.getSprite("border_top_left_1");
    Sprite borderTopRightSprite = spriteSheet.getSprite("border_top_right_2");

    Sprite borderBottomLeftSprite =
        spriteSheet.getSprite("border_bottom_left_1");
    Sprite borderBottomRightSprite =
        spriteSheet.getSprite("border_bottom_right_2");

    for (int y = gap; y <= canvasHeight / (blockSize); y++) {
      Image imageLeft = null;
      Image imageRight = null;

      if (y == gap) {
        imageLeft = borderTopLeftSprite.getImage();
        imageRight = borderTopRightSprite.getImage();
      } else if (y == canvasHeight / (blockSize)-1) {
        imageLeft = borderBottomLeftSprite.getImage();
        imageRight = borderBottomRightSprite.getImage();
      } else {
        imageLeft = borderLeftSprite.getImage();
        imageRight = borderRightSprite.getImage();
      }

      offGc.drawImage(imageLeft, 0, y, 1, 1);
      offGc.drawImage(imageRight, (canvasWidth / blockSize) - 1, y, 1, 1);
    }
  }

  private void drawInfo() {

    offGc.setFill(Color.ORANGE);
    offGc.fillRect(0, 0, canvasWidth / (blockSize), 2);

    offGc.setTextAlign(TextAlignment.CENTER);
    offGc.setFont(new Font("Bomberman", 1));
    double infoCenter = 0.5;

    // lifes
    List<Player> players = game.getPlayers();
    int gap = 4;
    for (int i = 0; i < players.size(); i++) {
      Player player = players.get(i);

      int axisX = i * gap;

      CharacterColor playerColor = player.getColor();
      String colorStr = playerColor.toString().toLowerCase();
      Sprite playerFaceSprite = spriteSheet.getSprite(colorStr + "_face");

      offGc.drawImage(playerFaceSprite.getImage(), axisX + 1, infoCenter, 1, 1);
      String lifesStr = player.getLifes().toString();
      offGc.setFill(Color.BLACK);
      offGc.fillRect(axisX + 2, infoCenter, 1, 1);
      offGc.setFill(Color.WHITE);
      offGc.fillText(lifesStr, axisX + 2.5, infoCenter + 0.85, 1);
    }
  }

  private void drawEnd() {
    offGc.setTransform(defaultTransform);

    offGc.setGlobalAlpha(0.75);
    offGc.setFill(Color.web("#000000"));
    offGc.fillRect(0, 0, canvasWidth, canvasHeight);

    offGc.setGlobalAlpha(1);

    offGc.setFill(Color.WHITE);
    offGc.setTextAlign(TextAlignment.CENTER);

    offGc.setFont(new Font("Bomberman", blockSize));
    offGc.fillText("Juego terminado", canvasWidth / 2,
                   canvasHeight / 2 - blockSize / 3);

    offGc.setFont(new Font("Bomberman", blockSize / 3));
    offGc.fillText("Esperando host", canvasWidth / 2,
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

    offGc.setFont(new Font("Bomberman", blockSize));
    offGc.fillText("Pausa", canvasWidth / 2, canvasHeight / 2 - blockSize / 3);

    offGc.setFont(new Font("Bomberman", blockSize / 3));
    offGc.fillText("Pulsa ESC para reanudar", canvasWidth / 2,
                   canvasHeight / 2 + blockSize / 3);

    offGc.scale(blockSize, blockSize);
  }

  private void drawMain() {
    Image offScreenImage = offCanvas.snapshot(null, null);
    mainGc.drawImage(offScreenImage, 0, 0);
  }
}

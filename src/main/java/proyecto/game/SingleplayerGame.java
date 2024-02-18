package proyecto.game;

import java.util.ArrayList;
import javafx.scene.input.KeyCode;
import proyecto.model.*;

public class SingleplayerGame {

  private static SingleplayerGame instance;
  private boolean isRunning = true;

  private SpriteSheet spriteSheet;
  private Level level;
  private Player player;
  private ArrayList<Bomb> bombs;
  private int ticksCount = 0;
  private double currentTimeMs = 0;
  KeyHandler keyHandler = KeyHandler.getInstance();

  private SingleplayerGame() {
    spriteSheet = SpriteSheet.getInstance();
    player = new Player(1, 1);
    player.setBombType(BombType.BASIC);
    bombs = new ArrayList<Bomb>();
    level = new Level(15, 13, player.getCoord());
  }

  public static SingleplayerGame getInstance() {
    if (instance == null) {
      instance = new SingleplayerGame();
    }
    return instance;
  }

  public void start() {
    keyHandler.onPressed(KeyCode.ENTER, () -> this.addBomb());
  }

  public boolean isRunning() { return isRunning; }

  public SpriteSheet getSpriteSheet() { return spriteSheet; }

  public Level getLevel() { return level; }

  public Player getPlayer() { return player; }

  public ArrayList<Bomb> getBombs() { return bombs; }

  public void movePlayer() {
    double speed = player.getSpeed();

    double movementStateX = 0;
    double movementStateY = 0;

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

    movePlayerX(movementStateX * speed);
    movePlayerY(movementStateY * speed);
  }

  private void movePlayerX(double speed) {
    Coord<Double> playerCoord = player.getCoord();
    Coord<Double> newCoord = new Coord<>(playerCoord.x, playerCoord.y);

    newCoord.x += speed;
    double x = speed > 0 ? Math.ceil(newCoord.x) : Math.floor(newCoord.x);

    if (!level.checkPlayerCollisionX((int)x, newCoord.y)) {
      player.setCoord(newCoord);
    }
  }

  private void movePlayerY(double speed) {
    Coord<Double> playerCoord = player.getCoord();
    Coord<Double> newCoord = new Coord<>(playerCoord.x, playerCoord.y);

    newCoord.y += speed;
    double y = speed > 0 ? Math.ceil(newCoord.y) : Math.floor(newCoord.y);

    if (!level.checkPlayerCollisionY(newCoord.x, (int)y)) {
      player.setCoord(newCoord);
    }
  }

  public void addBomb() {
    Coord<Double> playerCoord = player.getCoord();
    Bomb newBomb = new BasicBomb(Coord.round(playerCoord));

    // if (bombType == BombType.BASIC) {
    // newBomb = new BasicBomb(coords);
    // }

    int bombDelay = newBomb.getDelayTicks();
    newBomb.setExplosionTick(ticksCount + bombDelay);
    bombs.add(newBomb);
  }

  public void loop(double deltaMs) {
    calculateTick(deltaMs);

    movePlayer();

    for (int i = 0; i < bombs.size(); i++) {
      Bomb bomb = bombs.get(i);

      int removeBombsTicks = GameConstants.secondsToTicks(1);
      int removeTick = bomb.getExplosionTick() + removeBombsTicks;

      if (!bomb.exploded() && ticksCount >= bomb.getExplosionTick()) {
        bomb.explode(level);
      } else if (bomb.exploded() && ticksCount >= removeTick) {
        bombs.remove(i);
      }
    }
  }

  private void calculateTick(double deltaMs) {
    currentTimeMs += deltaMs;
    if (currentTimeMs >= GameConstants.TICK_DURATION_MS) {
      ticksCount++;
      currentTimeMs -= GameConstants.TICK_DURATION_MS;
    }
  }
}

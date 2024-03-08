package proyecto.game;

import java.util.ArrayList;
import javafx.scene.input.KeyCode;
import proyecto.model.*;
import proyecto.model.Character;

public class SingleplayerGame {

  private static SingleplayerGame instance;

  private GameState gameState = GameState.RUNNING;

  private SpriteSheet spriteSheet;
  private Level level;
  private Player player;
  private ArrayList<Bomb> bombs;
  private int ticksCount = 0;
  private double currentTimeMs = 0;
  private KeyHandler keyHandler = KeyHandler.getInstance();
  private Integer countDownMs = 0;

  private SingleplayerGame() {
    spriteSheet = SpriteSheet.getInstance();
    Coord<Double> playerCoord = new Coord<>(1.0, 1.0);
    player = new Player(playerCoord, 3, 1);
    player.setBombType(BombType.BASIC);
    bombs = new ArrayList<Bomb>();
    countDownMs = 120 * 1000;
    level = new Level(15, 13, player);
  }

  public static SingleplayerGame getInstance() {
    if (instance == null) {
      instance = new SingleplayerGame();
    }
    return instance;
  }

  public void start() {

    keyHandler.onPressed(KeyCode.ENTER, () -> {
      if (gameState == GameState.RUNNING) {
        this.addBomb();
      }
    });
  }

  public void end() { instance = null; }

  public Integer getCountDownMs() { return countDownMs; }

  public GameState getGameState() { return gameState; }

  public void setGameState(GameState gameState) { this.gameState = gameState; }

  public SpriteSheet getSpriteSheet() { return spriteSheet; }

  public Level getLevel() { return level; }

  public Player getPlayer() { return player; }

  public ArrayList<Bomb> getBombs() { return bombs; }

  public void movePlayer(double deltaMs) {

    Coord<Double> playerCoord = player.getCoord();
    Coord<Double> newCoord = new Coord<>(playerCoord.x, playerCoord.y);
    double speed = player.getSpeed();

    if (!(keyHandler.isDown(KeyCode.A) || keyHandler.isDown(KeyCode.D) ||
          keyHandler.isDown(KeyCode.W) || keyHandler.isDown(KeyCode.S))) {
      player.setDirectionStateCounter(0);
      player.setDirectionState(1);
    }

    if (player.getDirectionStateCounter() >= 10) {
      int directionState = player.getDirectionState();

      if (directionState < 3) {
        player.setDirectionState(directionState + 1);
      } else {
        player.setDirectionState(1);
      }

      player.setDirectionStateCounter(0);
    }
    player.setDirectionStateCounter(player.getDirectionStateCounter() + 1);

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

    if (movementStateX > 0) {
      player.setDirection(Direction.RIGHT);
    } else if (movementStateX < 0) {
      player.setDirection(Direction.LEFT);
    }

    if (movementStateY > 0) {
      player.setDirection(Direction.DOWN);
    } else if (movementStateY < 0) {
      player.setDirection(Direction.UP);
    }

    Double speedX = movementStateX * speed;
    Double speedY = movementStateY * speed;

    Double newX = newCoord.x + speedX;
    Double newY = newCoord.y + speedY;

    double x = speedX > 0 ? Math.ceil(newX) : Math.floor(newX);
    double y = speedY > 0 ? Math.ceil(newY) : Math.floor(newY);

    if (!level.checkCollisionX((int)x, newCoord.y)) {
      newCoord.x = newX;
    }

    Character collidedX = level.checkCharacterCollisions((int)x, newCoord.y);

    if (collidedX instanceof Enemy && !player.isInvincible()) {
      player.setLifes(player.getLifes() - 1);
      Integer invincibilityTicks = GameConstants.secondsToTicks(2);
      player.makeInvincibleUntil(ticksCount + invincibilityTicks);
    }

    Integer newCoordY = (int)Math.round(newCoord.y);
    Entity<Integer> collidedEntityX =
        level.checkEntityCollision((int)x, newCoordY);

    if (collidedEntityX instanceof Bomb) {
      newCoord.x = playerCoord.x;
    }

    if (collidedEntityX instanceof PowerUp) {
      AirBlock entityBlock = (AirBlock)level.getBlock((int)x, newCoordY);
      entityBlock.setEntity(null);

      ((PowerUp)collidedEntityX).activate(player);
    }

    if (!level.checkCollisionY(newCoord.x, (int)y)) {
      newCoord.y = newY;
    }

    Character collidedY = level.checkCharacterCollisions(newCoord.x, (int)y);

    if (collidedY instanceof Enemy && !player.isInvincible()) {
      player.setLifes(player.getLifes() - 1);
      Integer invincibilityTicks = GameConstants.secondsToTicks(2);
      player.makeInvincibleUntil(ticksCount + invincibilityTicks);
    }

    Integer newCoordX = (int)Math.round(newCoord.x);

    Entity<Integer> collidedEntityY =
        level.checkEntityCollision(newCoordX, (int)y);

    if (collidedEntityY instanceof Bomb) {
      newCoord.y = playerCoord.y;
    }

    if (collidedEntityY instanceof PowerUp) {
      AirBlock entityBlock = (AirBlock)level.getBlock(newCoordX, (int)y);
      entityBlock.setEntity(null);
      ((PowerUp)collidedEntityY).activate(player);
    }

    player.setCoord(newCoord);
  }

  public void moveCharacters(double deltaMs) {
    for (Character character : level.getCharacters()) {
      if (character instanceof Player) {
        continue;
      }

      if (character.isDead()) {
        continue;
      }

      if (character.getDirectionStateCounter() >= 10) {
        int directionState = character.getDirectionState();

        if (directionState < 4) {
          character.setDirectionState(directionState + 1);
        } else {
          character.setDirectionState(1);
        }

        character.setDirectionStateCounter(0);
      }
      character.setDirectionStateCounter(character.getDirectionStateCounter() +
                                         1);

      Direction direction = character.getDirection();
      Double speed = character.getSpeed();
      Coord<Double> characterCoord = character.getCoord();
      Coord<Double> newCoord = new Coord<>(characterCoord.x, characterCoord.y);

      if (direction == Direction.LEFT || direction == Direction.RIGHT) {
        int movementStateX = direction == Direction.RIGHT ? 1 : -1;

        Double speedX = movementStateX * speed;

        Double newX = newCoord.x + speedX;
        double x = speedX > 0 ? Math.ceil(newX) : Math.floor(newX);

        Integer newCoordY = (int)Math.round(newCoord.y);
        Entity<Integer> collidedEntityX =
            level.checkEntityCollision((int)x, newCoordY);

        if (level.checkCollisionX((int)x, newCoord.y) ||
            collidedEntityX instanceof Bomb) {
          Direction newDirection = direction.getOpposite();
          character.setDirection(newDirection);
          continue;
        } else {
          newCoord.x = newX;
          character.setCoord(newCoord);
        }
      }

      if (direction == Direction.UP || direction == Direction.DOWN) {
        int movementStateY = direction == Direction.DOWN ? 1 : -1;
        Double speedY = movementStateY * speed;
        Double newY = newCoord.y + speedY;
        double y = speedY > 0 ? Math.ceil(newY) : Math.floor(newY);

        Integer newCoordX = (int)Math.round(newCoord.x);
        Entity<Integer> collidedEntityY =
            level.checkEntityCollision(newCoordX, (int)y);

        if (level.checkCollisionY(newCoord.x, (int)y) ||
            collidedEntityY instanceof Bomb) {
          Direction newDirection = direction.getOpposite();
          character.setDirection(newDirection);
        } else {
          newCoord.y = newY;
          character.setCoord(newCoord);
        }
      }
    }
  }

  public void addBomb() {
    Coord<Double> playerCoord = player.getCoord();
    Integer availableBombs = player.getAvailableBombs();

    if (availableBombs <= 0) {
      return;
    }

    Coord<Integer> bombCoord = Coord.round(playerCoord);

    Block block = level.getBlock(bombCoord);

    if (!(block instanceof AirBlock)) {
      return;
    }

    AirBlock airBlock = (AirBlock)block;

    if (airBlock.hasEntity()) {
      return;
    }

    Bomb newBomb = new BasicBomb(bombCoord, player);

    // if (bombType == BombType.BASIC) {
    // newBomb = new BasicBomb(coords);
    // }

    int bombDelay = newBomb.getDelayTicks();
    newBomb.setExplosionTick(ticksCount + bombDelay);
    bombs.add(newBomb);
    airBlock.setEntity(newBomb);
    player.setAvailableBombs(availableBombs - 1);
  }

  public void loop(double deltaMs) {
    if (gameState != GameState.RUNNING) {
      return;
    }

    if (player.isDead() || countDownMs <= 0) {
      gameState = GameState.GAMEOVER;
      return;
    }

    boolean allEnemiesDead = true;
    for (Character enemy : level.getCharacters()) {
      if (!(enemy instanceof Enemy)) {
        continue;
      }
      if (!enemy.isDead()) {
        allEnemiesDead = false;
        break;
      }
    }

    if (allEnemiesDead) {
      gameState = GameState.WIN;
      return;
    }

    countDownMs -= (int)Math.round(deltaMs);
    calculateTick(deltaMs);
    handleInvencibility();
    movePlayer(deltaMs);
    moveCharacters(deltaMs);
    handleBombs();
  }

  private void handleInvencibility() {
    Integer playerInvencibilityTicks = player.getInvincibilityTicks();

    if (playerInvencibilityTicks == -1) {
      return;
    }

    if (playerInvencibilityTicks <= ticksCount) {
      player.setInvincible(false);
      player.setInvincibilityTicks(-1);
    }
  }

  private void handleBombs() {
    for (int i = 0; i < bombs.size(); i++) {
      Bomb bomb = bombs.get(i);

      int removeBombsTicks = GameConstants.secondsToTicks(0.5);
      int removeTick = bomb.getExplosionTick() + removeBombsTicks;

      if (!bomb.exploded() && ticksCount >= bomb.getExplosionTick()) {
        bomb.explode(level);
      } else if (bomb.exploded() && ticksCount >= removeTick) {
        bombs.remove(i);

        AirBlock airBlock = (AirBlock)level.getBlock(bomb.getCoord());
        airBlock.setEntity(null);
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

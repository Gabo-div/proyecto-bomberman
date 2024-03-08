
package proyecto.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import proyecto.model.*;
import proyecto.multiplayer.User;

public class MultiplayerGame {

  private static MultiplayerGame instance;

  private GameState gameState = GameState.NONE;

  private ArrayList<Player> players = new ArrayList<>();

  private Level level;
  private ArrayList<Bomb> bombs;
  private int ticksCount = 0;
  private double currentTimeMs = 0;

  private MultiplayerGame() {}

  public static MultiplayerGame getInstance() {
    if (instance == null) {
      instance = new MultiplayerGame();
    }
    return instance;
  }

  public void start(List<User> users) {
    players = new ArrayList<>();
    int playersCount = 1;
    int levelWidth = 15;
    int levelHeight = 13;

    for (User user : users) {
      Coord<Double> playerCoord = new Coord<>(1.0, 1.0);

      if (playersCount == 2) {
        playerCoord = new Coord<>(levelWidth - 2.0, 1.0);
      } else if (playersCount == 3) {
        playerCoord = new Coord<>(1.0, levelHeight - 2.0);
      } else if (playersCount == 4) {
        playerCoord = new Coord<>(levelWidth - 2.0, levelHeight - 2.0);
      }

      Player player = new Player(playerCoord, 3, 1);
      player.setName(user.getName());
      player.setColor(user.getColor());
      players.add(player);
      playersCount++;
    }

    bombs = new ArrayList<Bomb>();
    level = new Level(levelWidth, levelHeight, players);

    gameState = GameState.RUNNING;
  }

  public void start() {
    gameState = GameState.RUNNING;
    bombs = new ArrayList<Bomb>();
    level = new Level(15, 13, players);
  }

  public void end() { instance = null; }

  public GameState getGameState() { return gameState; }

  public void setGameState(GameState gameState) { this.gameState = gameState; }

  public Level getLevel() { return level; }

  public ArrayList<Player> getPlayers() { return players; }

  public Player getPlayer(String name) {
    for (Player player : players) {
      if (player.getName().equals(name)) {
        return player;
      }
    }
    return null;
  }

  public ArrayList<Bomb> getBombs() { return bombs; }

  public void handlePlayers(double deltaMs) {
    for (Player player : players) {
      handlePlayerInvincibility(player);
      handlePlayerMovement(player, deltaMs);
    }
  }

  private void handlePlayerInvincibility(Player player) {
    Integer playerInvencibilityTicks = player.getInvincibilityTicks();

    if (playerInvencibilityTicks == -1) {
      return;
    }

    if (playerInvencibilityTicks <= ticksCount) {
      player.setInvincible(false);
      player.setInvincibilityTicks(-1);
    }
  }

  private void handlePlayerMovement(Player player, double deltaMs) {
    if (player.isDead()) {
      return;
    }

    Coord<Double> playerCoord = player.getCoord();
    Coord<Double> newCoord = new Coord<>(playerCoord.x, playerCoord.y);
    double speed = player.getSpeed();

    if (player.getMovementStateX() == 0 && player.getMovementStateY() == 0) {
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

    double movementStateX = player.getMovementStateX();
    double movementStateY = player.getMovementStateY();

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

  public void addBomb(String playerName) {
    Player player = getPlayer(playerName);

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

    calculateTick(deltaMs);

    handlePlayers(deltaMs);
    handleBombs();
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

  public void syncPlayers(Collection<Player> players) {
    this.players = new ArrayList<>(players);
  }

  public void syncBombs(Collection<Bomb> bombs) {
    this.bombs = new ArrayList<>(bombs);
  }

  public void syncLevel(Level level) { this.level = level; }
}

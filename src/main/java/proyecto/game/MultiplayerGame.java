package proyecto.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import proyecto.model.*;
import proyecto.multiplayer.User;

/**
 * Clase que representa el juego multijugador.
 */
public class MultiplayerGame {

  private static MultiplayerGame instance;

  private GameState gameState = GameState.NONE;

  private ArrayList<Player> players = new ArrayList<>();

  private Level level;
  private ArrayList<Bomb> bombs;
  private int ticksCount = 0;
  private double currentTimeMs = 0;

  private MultiplayerGame() {}

  /**
   * Devuelve la instancia única del juego multijugador.
   * @return La instancia del juego multijugador.
   */
  public static MultiplayerGame getInstance() {
    if (instance == null) {
      instance = new MultiplayerGame();
    }
    return instance;
  }

  /**
   * Inicia el juego multijugador con los usuarios dados.
   * @param users La lista de usuarios que participarán en el juego.
   */
  public void start(List<User> users) {
    players = new ArrayList<>();
    int playersCount = 1;
    int levelWidth = 15;
    int levelHeight = 13;

    // Asigna posiciones iniciales a los jugadores basadas en el número de usuarios
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

  /**
   * Inicia el juego multijugador sin usuarios.
   */
  public void start() {
    bombs = new ArrayList<Bomb>();
    level = new Level(15, 13, players);

    gameState = GameState.RUNNING;
  }

  /**
   * Finaliza el juego multijugador.
   */
  public void end() { gameState = GameState.NONE; }

  /**
   * Obtiene el estado actual del juego.
   * @return El estado actual del juego.
   */
  public GameState getGameState() { return gameState; }

  /**
   * Establece el estado del juego.
   * @param gameState El estado del juego.
   */
  public void setGameState(GameState gameState) { this.gameState = gameState; }

  /**
   * Obtiene el nivel actual del juego.
   * @return El nivel actual del juego.
   */
  public Level getLevel() { return level; }

  /**
   * Obtiene la lista de jugadores del juego.
   * @return La lista de jugadores del juego.
   */
  public ArrayList<Player> getPlayers() { return players; }

  /**
   * Obtiene un jugador específico por su nombre.
   * @param name El nombre del jugador.
   * @return El jugador con el nombre especificado, o null si no se encuentra.
   */
  public Player getPlayer(String name) {
    for (Player player : players) {
      if (player.getName().equals(name)) {
        return player;
      }
    }
    return null;
  }

  /**
   * Obtiene la lista de bombas en el juego.
   * @return La lista de bombas en el juego.
   */
  public ArrayList<Bomb> getBombs() { return bombs; }

  /**
   * Maneja la lógica de los jugadores en el juego.
   * @param deltaMs El tiempo transcurrido desde la última actualización en milisegundos.
   */
  public void handlePlayers(double deltaMs) {
    for (Player player : players) {
      handlePlayerInvincibility(player);
      handlePlayerMovement(player, deltaMs);
    }
  }

  /**
   * Maneja la invencibilidad de un jugador.
   * @param player El jugador cuya invencibilidad se debe manejar.
   */
  private void handlePlayerInvincibility(Player player) {
    Integer playerInvincibilityTicks = player.getInvincibilityTicks();

    if (playerInvincibilityTicks == -1) {
      return;
    }

    if (playerInvincibilityTicks <= ticksCount) {
      player.setInvincible(false);
      player.setInvincibilityTicks(-1);
    }
  }

  /**
   * Maneja el movimiento de un jugador.
   * @param player El jugador cuyo movimiento se debe manejar.
   * @param deltaMs El tiempo transcurrido desde la última actualización en milisegundos.
   */
  private void handlePlayerMovement(Player player, double deltaMs) {
    // Lógica de movimiento de jugador
  }

  /**
   * Agrega una bomba colocada por un jugador.
   * @param playerName El nombre del jugador que coloca la bomba.
   */
  public void addBomb(String playerName) {
    // Lógica para agregar una bomba al juego
  }

  /**
   * Ejecuta un ciclo de juego.
   * @param deltaMs El tiempo transcurrido desde la última actualización en milisegundos.
   */
  public void loop(double deltaMs) {
    // Lógica principal del ciclo de juego
  }

  /**
   * Sincroniza los jugadores en el juego con una colección de jugadores externos.
   * @param players La colección de jugadores externos.
   */
  public void syncPlayers(Collection<Player> players) {
    this.players = new ArrayList<>(players);
  }

  /**
   * Sincroniza las bombas en el juego con una colección de bombas externas.
   * @param bombs La colección de bombas externas.
   */
  public void syncBombs(Collection<Bomb> bombs) {
    this.bombs = new ArrayList<>(bombs);
  }

  /**
   * Sincroniza el nivel del juego con otro nivel externo.
   * @param level El nivel externo con el que se sincroniza.
   */
  public void syncLevel(Level level) { this.level = level; }
}

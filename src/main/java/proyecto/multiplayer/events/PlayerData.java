package proyecto.multiplayer.events;

import java.io.Serializable;
import proyecto.model.Direction;
import proyecto.model.Player;
import proyecto.multiplayer.CharacterColor;

/**
 * Clase que representa los datos de un jugador para la transmisión en red.
 */
public class PlayerData implements Serializable {
  public String username;        // Nombre de usuario del jugador
  public CharacterColor color;   // Color del jugador
  public Direction direction;    // Dirección actual del jugador
  public Integer directionState; // Estado de la dirección del jugador
  public Boolean invincible;     // Estado de invencibilidad del jugador
  public Integer lifes;          // Vidas restantes del jugador
  public Double x;               // Posición X del jugador
  public Double y;               // Posición Y del jugador

  /**
   * Constructor que inicializa los datos de un jugador.
   * @param username Nombre de usuario del jugador.
   * @param player Instancia del jugador.
   */
  public PlayerData(String username, Player player) {
    this.username = username;
    this.color = player.getColor();
    this.direction = player.getDirection();
    this.directionState = player.getDirectionState();
    this.invincible = player.isInvincible();
    this.lifes = player.getLifes();
    this.x = player.getCoord().x;
    this.y = player.getCoord().y;
  }
}

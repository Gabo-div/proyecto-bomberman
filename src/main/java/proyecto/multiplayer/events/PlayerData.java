package proyecto.multiplayer.events;

import java.io.Serializable;
import proyecto.model.Direction;
import proyecto.model.Player;
import proyecto.multiplayer.CharacterColor;

public class PlayerData implements Serializable {
  public String username;
  public CharacterColor color;
  public Direction direction;
  public Integer directionState;
  public Boolean invincible;
  public Integer lifes;
  public Double x;
  public Double y;

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

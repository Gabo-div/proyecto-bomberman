package proyecto.multiplayer.events;

import java.io.Serializable;
import proyecto.model.Bomb;

public class BombData implements Serializable {
  public Boolean exploded;
  public Integer x;
  public Integer y;

  public BombData(Bomb bomb) {
    exploded = bomb.exploded();
    x = bomb.getCoord().x;
    y = bomb.getCoord().y;
  }
}

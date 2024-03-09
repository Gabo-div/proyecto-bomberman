package proyecto.multiplayer.events;

import java.io.Serializable;
import proyecto.model.Bomb;

/**
 * Clase que representa los datos de una bomba para la transmisión en red.
 */
public class BombData implements Serializable {
  public Boolean exploded;
  public Integer x;
  public Integer y;

  /**
   * Constructor que inicializa los datos de la bomba.
   * 
   * @param bomb La bomba de la cual se obtienen los datos.
   */
  public BombData(Bomb bomb) {
    exploded = bomb.exploded();
    x = bomb.getCoord().x;
    y = bomb.getCoord().y;
  }
}

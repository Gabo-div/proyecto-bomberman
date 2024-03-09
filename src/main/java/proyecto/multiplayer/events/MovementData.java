package proyecto.multiplayer.events;

import java.io.Serializable;

/**
 * Clase que representa los datos de movimiento para la transmisión en red.
 */
public class MovementData implements Serializable {
  public Integer stateX;
  public Integer stateY;
}

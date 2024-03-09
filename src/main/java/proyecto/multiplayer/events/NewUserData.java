package proyecto.multiplayer.events;

import java.io.Serializable;
import proyecto.multiplayer.CharacterColor;

/**
 * Clase que representa los datos de un nuevo usuario para la transmisión en red.
 */
public class NewUserData implements Serializable {
  public String name;          // Nombre del nuevo usuario
  public CharacterColor color; // Color asignado al nuevo usuario
}

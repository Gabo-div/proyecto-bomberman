package proyecto.multiplayer.events;

import java.io.Serializable;
import java.util.List;

/**
 * Clase que representa los datos de los usuarios para la transmisión en red.
 */
public class UsersData implements Serializable {
  public List<NewUserData> users; // Lista de datos de nuevos usuarios
}

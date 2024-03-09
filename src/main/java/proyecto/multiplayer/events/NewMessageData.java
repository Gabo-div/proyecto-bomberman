package proyecto.multiplayer.events;

import java.io.Serializable;

/**
 * Clase que representa los datos de un nuevo mensaje para la transmisión en red.
 */
public class NewMessageData implements Serializable {
  public String message; // Contenido del mensaje
  public String name;    // Nombre del remitente
}

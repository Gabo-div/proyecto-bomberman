package proyecto.multiplayer;

/**
 * Clase que representa un mensaje enviado por un usuario en el juego multijugador.
 */
public class Message {
  private String message; // Contenido del mensaje
  private User user; // Usuario que envió el mensaje

  /**
   * Constructor de la clase Message.
   * @param message Contenido del mensaje.
   * @param user Usuario que envió el mensaje.
   */
  public Message(String message, User user) {
    this.message = message;
    this.user = user;
  }

  /**
   * Método para obtener el contenido del mensaje.
   * @return El contenido del mensaje.
   */
  public String getMessage() { return message; }

  /**
   * Método para obtener el usuario que envió el mensaje.
   * @return El usuario que envió el mensaje.
   */
  public User getUser() { return user; }
}

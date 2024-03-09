package proyecto.multiplayer;

/**
 * Clase que representa un usuario en el juego multijugador.
 */
public class User {
  private String name; // Nombre del usuario
  private CharacterColor color; // Color del personaje del usuario

  /**
   * Constructor de la clase User.
   * @param name Nombre del usuario.
   * @param color Color del personaje del usuario.
   */
  public User(String name, CharacterColor color) {
    this.name = name;
    this.color = color;
  }

  /**
   * Obtiene el nombre del usuario.
   * @return El nombre del usuario.
   */
  public String getName() {
    return name;
  }

  /**
   * Obtiene el color del personaje del usuario.
   * @return El color del personaje del usuario.
   */
  public CharacterColor getColor() {
    return color;
  }

  /**
   * Establece el color del personaje del usuario.
   * @param color El nuevo color del personaje del usuario.
   */
  public void setColor(CharacterColor color) {
    this.color = color;
  }
}

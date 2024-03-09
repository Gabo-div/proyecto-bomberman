package proyecto.model;

/**
 * Enumeración que representa los tipos de power-ups disponibles en el juego.
 */
public enum PowerUpType {
  FIREUP, // Aumenta la potencia de las explosiones de las bombas.
  BOMBUP, // Aumenta el número máximo de bombas que un jugador puede colocar.
  LIFEUP; // Aumenta la cantidad de vidas de un jugador.

  /**
   * Método estático que devuelve un tipo de power-up aleatorio.
   * @return Tipo de power-up aleatorio.
   */
  public static PowerUpType random() {
    return values()[(int)(Math.random() * values().length)];
  }
}

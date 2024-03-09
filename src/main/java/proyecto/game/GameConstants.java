package proyecto.game;

/**
 * Clase que contiene constantes y métodos de utilidad para el juego.
 */
public class GameConstants {
  
  /** 
   * Duración de un tick en milisegundos. 
   * */
  static public final int TICK_DURATION_MS = 50;

  /**
   * Convierte segundos en ticks.
   * @param seconds El número de segundos a convertir.
   * @return El equivalente en ticks.
   */
  static public int secondsToTicks(int seconds) {
    return seconds * 1000 / TICK_DURATION_MS;
  }

  /**
   * Convierte segundos en ticks.
   * @param seconds El número de segundos a convertir.
   * @return El equivalente en ticks.
   */
  static public int secondsToTicks(double seconds) {
    return (int)Math.round(seconds * 1000 / TICK_DURATION_MS);
  }
}

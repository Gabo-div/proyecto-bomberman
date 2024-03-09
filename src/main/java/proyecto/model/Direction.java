package proyecto.model;

/**
 * Enumeración que representa las direcciones posibles en el juego.
 */
public enum Direction {
  /**
   * Dirección hacia arriba.
   */
  UP,
  
  /**
   * Dirección hacia abajo.
   */
  DOWN,
  
  /**
   * Dirección hacia la izquierda.
   */
  LEFT,
  
  /**
   * Dirección hacia la derecha.
   */
  RIGHT;

  /**
   * Genera una dirección aleatoria.
   * @return Una dirección aleatoria.
   */
  public static Direction random() {
    return values()[(int)(Math.random() * values().length)];
  }

  /**
   * Genera una dirección aleatoria en el eje x.
   * @return Una dirección aleatoria en el eje x.
   */
  public static Direction randomX() {
    return (Math.random() < 0.5) ? LEFT : RIGHT;
  }

  /**
   * Genera una dirección aleatoria en el eje y.
   * @return Una dirección aleatoria en el eje y.
   */
  public static Direction randomY() {
    return (Math.random() < 0.5) ? UP : DOWN;
  }

  /**
   * Devuelve la dirección opuesta a la actual.
   * @return La dirección opuesta.
   */
  public Direction getOpposite() {
    switch (this) {
      case UP:
        return DOWN;
      case DOWN:
        return UP;
      case LEFT:
        return RIGHT;
      case RIGHT:
        return LEFT;
      default:
        throw new RuntimeException("Unknown direction");
    }
  }
}

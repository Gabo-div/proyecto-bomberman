package proyecto.model;

/**
 * Clase abstracta que representa una bomba en el juego.
 *
 * @param <Integer> El tipo de dato para las coordenadas de la bomba.
 */
public abstract class Bomb extends Entity<Integer> {

  protected Coord<Integer> coord; // Coordenadas de la bomba
  protected boolean exploded = false; // Indica si la bomba ha explotado
  protected int explosionTick = 0; // Tiempo de la explosión
  protected Player player; // Jugador que colocó la bomba

  /**
   * Constructor de la clase Bomba.
   *
   * @param coord  Las coordenadas de la bomba.
   * @param player El jugador que colocó la bomba.
   */
  public Bomb(Coord<Integer> coord, Player player) {
    this.coord = coord;
    this.player = player;
  }

  /**
   * Obtiene las coordenadas de la bomba.
   *
   * @return Las coordenadas de la bomba.
   */
  @Override
  public Coord<Integer> getCoord() {
    return new Coord<>(coord.x, coord.y);
  }

  /**
   * Comprueba si la bomba ha explotado.
   *
   * @return Verdadero si la bomba ha explotado, falso de lo contrario.
   */
  public boolean exploded() {
    return exploded;
  }

  /**
   * Obtiene el tiempo de explosión de la bomba.
   *
   * @return El tiempo de explosión de la bomba.
   */
  public int getExplosionTick() {
    return explosionTick;
  }

  /**
   * Establece el tiempo de explosión de la bomba.
   *
   * @param explosionTick El tiempo de explosión de la bomba.
   */
  public void setExplosionTick(int explosionTick) {
    this.explosionTick = explosionTick;
  }

  /**
   * Obtiene el radio de la explosión de la bomba.
   *
   * @return El radio de la explosión de la bomba.
   */
  public int getRadius() {
    return player.getFirepower();
  }

  /**
   * Método abstracto para realizar la explosión de la bomba en el nivel.
   *
   * @param level El nivel en el que se produce la explosión.
   */
  public abstract void explode(Level level);

  /**
   * Método abstracto para obtener el tiempo de retraso en ticks antes de la explosión de la bomba.
   *
   * @return El tiempo de retraso en ticks antes de la explosión de la bomba.
   */
  public abstract int getDelayTicks();
}

package proyecto.model;

/**
 * Clase abstracta que representa un power-up en el juego.
 */
public abstract class PowerUp extends Entity<Integer> {
  protected Coord<Integer> coord;

  /**
   * Constructor para crear un power-up con una coordenada.
   * @param coord Coordenada del power-up.
   */
  public PowerUp(Coord<Integer> coord) { this.coord = coord; }

  /**
   * Método abstracto para activar el efecto del power-up en un jugador.
   * @param player Jugador al que se le aplica el power-up.
   */
  public abstract void activate(Player player);

  @Override
  public Coord<Integer> getCoord() {
    return new Coord<>(coord.x, coord.y);
  }
}

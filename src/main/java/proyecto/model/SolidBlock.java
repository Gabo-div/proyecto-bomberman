package proyecto.model;

/**
 * Clase abstracta que representa un bloque sólido en el juego.
 */
public abstract class SolidBlock extends Block {

  /** Coordenadas del bloque sólido. */
  Coord<Integer> coord;

  /**
   * Constructor de la clase SolidBlock.
   * @param coord Coordenadas del bloque sólido.
   */
  public SolidBlock(Coord<Integer> coord) { this.coord = coord; }

  /**
   * Método abstracto que indica si el bloque sólido puede romperse.
   * @return true si el bloque puede romperse, false de lo contrario.
   */
  public abstract boolean isBreakable();

  /**
   * Devuelve las coordenadas del bloque sólido.
   * @return Coordenadas del bloque sólido.
   */
  @Override
  public Coord<Integer> getCoord() {
    return coord;
  }
}

package proyecto.model;

/**
 * Clase que representa un bloque de pared en el juego, que es sólido y no se puede romper.
 */
public class WallBlock extends SolidBlock {

  /**
   * Constructor de la clase WallBlock.
   * @param coord La coordenada del bloque en el mapa.
   */
  public WallBlock(Coord<Integer> coord) { super(coord); }

  /**
   * Verifica si el bloque de pared es rompible.
   * @return Siempre devuelve false, ya que los bloques de pared no son rompibles.
   */
  @Override
  public boolean isBreakable() {
    return false;
  }
}

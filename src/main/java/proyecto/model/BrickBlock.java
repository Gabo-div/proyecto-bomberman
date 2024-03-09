package proyecto.model;

/**
 * Esta clase representa un bloque de ladrillo en el juego, que es rompible.
 */
public class BrickBlock extends SolidBlock {

  /**
   * La entidad asociada al bloque de ladrillo.
   */
  Entity<Integer> entity;

  /**
   * Constructor para inicializar el bloque de ladrillo en una coordenada específica.
   * @param coord La coordenada en la que se encuentra el bloque de ladrillo.
   */
  public BrickBlock(Coord<Integer> coord) {
    super(coord);
  }

  /**
   * Método para obtener la entidad asociada al bloque de ladrillo.
   * @return La entidad asociada al bloque de ladrillo.
   */
  public Entity<Integer> getEntity() {
    return entity;
  }

  /**
   * Método para establecer la entidad asociada al bloque de ladrillo.
   * @param entity La entidad a establecer.
   */
  public void setEntity(Entity<Integer> entity) {
    this.entity = entity;
  }

  /**
   * Método para verificar si el bloque de ladrillo es rompible.
   * @return Verdadero si el bloque de ladrillo es rompible, falso de lo contrario.
   */
  @Override
  public boolean isBreakable() {
    return true;
  }
}

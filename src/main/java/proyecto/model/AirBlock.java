package proyecto.model;

/**
 * Representa un bloque de aire en el juego.
 */
public class AirBlock extends Block {

    /** Entidad asociada al bloque de aire. */
    Entity<Integer> entity;
    
    /** Coordenada del bloque de aire. */
    Coord<Integer> coord;

    /**
     * Constructor de la clase AirBlock.
     *
     * @param coord La coordenada del bloque de aire.
     */
    public AirBlock(Coord<Integer> coord) {
        this.coord = coord;
    }

    /**
     * Obtiene la coordenada del bloque de aire.
     *
     * @return La coordenada del bloque de aire.
     */
    @Override
    public Coord<Integer> getCoord() {
        return coord;
    }

    /**
     * Obtiene la entidad asociada al bloque de aire.
     *
     * @return La entidad asociada al bloque de aire.
     */
    public Entity<Integer> getEntity() {
        return entity;
    }

    /**
     * Establece la entidad asociada al bloque de aire.
     *
     * @param entity La entidad a establecer.
     */
    public void setEntity(Entity<Integer> entity) {
        this.entity = entity;
    }

    /**
     * Verifica si el bloque de aire tiene una entidad asociada.
     *
     * @return true si el bloque de aire tiene una entidad asociada, false en caso contrario.
     */
    public boolean hasEntity() {
        return entity != null;
    }
}

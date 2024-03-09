package proyecto.model;

/**
 * Representa un enemigo básico en el juego.
 */
public class BasicEnemy extends Enemy {

    /**
     * Constructor de un enemigo básico.
     *
     * @param coord La coordenada del enemigo.
     */
    public BasicEnemy(Coord<Double> coord) {
        super(coord, 0.25 / 4, 1);
    }
}

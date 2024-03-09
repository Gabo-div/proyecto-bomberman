package proyecto.model;

/**
 * Clase abstracta que representa a un enemigo en el juego.
 */
public abstract class Enemy extends Character {
  
  /**
   * Constructor de la clase Enemy.
   * @param coord Coordenadas del enemigo.
   * @param speed Velocidad del enemigo.
   * @param lifes Vidas del enemigo.
   */
  public Enemy(Coord<Double> coord, double speed, int lifes) {
    super(coord, speed, lifes);
  }
}

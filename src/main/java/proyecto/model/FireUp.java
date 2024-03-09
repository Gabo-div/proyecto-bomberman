package proyecto.model;

/**
 * Clase que representa un power-up que aumenta la potencia de la explosión de las bombas del jugador.
 */
public class FireUp extends PowerUp {

  /**
   * Constructor para crear un power-up FireUp en una coordenada específica.
   * @param coord Coordenadas donde se encuentra el power-up.
   */
  public FireUp(Coord<Integer> coord) {
    super(coord);
  }

  /**
   * Método para activar el power-up y aumentar la potencia de la explosión de las bombas del jugador.
   * @param player Jugador al que se le aplica el power-up.
   */
  @Override
  public void activate(Player player) {
    player.addFirepower(1);
  }
}

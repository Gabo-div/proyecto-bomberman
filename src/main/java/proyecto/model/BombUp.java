package proyecto.model;

/**
 * Esta clase representa un power-up que aumenta la cantidad de bombas disponibles para un jugador.
 */
public class BombUp extends PowerUp {

  /**
   * Constructor para inicializar el power-up en una coordenada específica.
   * @param coord La coordenada en la que se encuentra el power-up.
   */
  public BombUp(Coord<Integer> coord) {
    super(coord);
  }

  /**
   * Método para activar el efecto del power-up en un jugador.
   * Aumenta en 1 la cantidad de bombas disponibles para el jugador.
   * @param player El jugador al que se le aplica el power-up.
   */
  @Override
  public void activate(Player player) {
    player.setAvailableBombs(player.getAvailableBombs() + 1);
  }
}

package proyecto.model;

/**
 * Clase que representa un power-up que otorga una vida adicional al jugador.
 */
public class LifeUp extends PowerUp {

  /**
   * Constructor para crear un power-up de vida adicional en una coordenada específica.
   * @param coord Coordenada donde se coloca el power-up.
   */
  public LifeUp(Coord<Integer> coord) {
    super(coord);
  }

  /**
   * Método para activar el power-up y otorgar una vida adicional al jugador.
   * @param player Jugador al que se le otorga la vida adicional.
   */
  @Override
  public void activate(Player player) {
    player.setLifes(player.getLifes() + 1);
  }
}

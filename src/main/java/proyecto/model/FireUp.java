package proyecto.model;

public class FireUp extends PowerUp {
  public FireUp(Coord<Integer> coord) { super(coord); }

  @Override
  public void activate(Player player) {
    player.addFirepower(1);
  }
}

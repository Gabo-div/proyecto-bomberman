package proyecto.model;

public class LifeUp extends PowerUp {
  public LifeUp(Coord<Integer> coord) { super(coord); }

  @Override
  public void activate(Player player) {
    player.setLifes(player.getLifes() + 1);
  }
}

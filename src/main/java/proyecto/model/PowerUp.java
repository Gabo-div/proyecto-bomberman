package proyecto.model;

public abstract class PowerUp extends Entity<Integer> {
  protected Coord<Integer> coord;

  public PowerUp(Coord<Integer> coord) { this.coord = coord; }

  @Override
  public Coord<Integer> getCoord() {
    return new Coord<>(coord.x, coord.y);
  }

  public abstract void activate(Player player);
}

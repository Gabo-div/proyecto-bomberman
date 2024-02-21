package proyecto.model;

public abstract class Bomb extends Entity<Integer> {

  protected Coord<Integer> coord;
  protected boolean exploded = false;
  protected int explosionTick = 0;
  protected int firepower;

  public Bomb(Coord<Integer> coord, int firepower) {
    this.coord = coord;
    this.firepower = firepower;
  }

  @Override
  public Coord<Integer> getCoord() {
    return new Coord<>(coord.x, coord.y);
  }

  public boolean exploded() { return exploded; }

  public int getExplosionTick() { return explosionTick; }

  public void setExplosionTick(int explosionTick) {
    this.explosionTick = explosionTick;
  }

  public int getRadius() { return firepower; }

  public abstract void explode(Level level);

  public abstract int getDelayTicks();
}

package proyecto.model;

public abstract class Bomb {

  protected Coord<Integer> coord;
  protected boolean exploded = false;
  protected int explosionTick = 0;

  public Bomb(Coord<Integer> coord) { this.coord = coord; }

  public Coord<Integer> getCoord() { return coord; }

  public boolean exploded() { return exploded; }

  public int getExplosionTick() { return explosionTick; }

  public void setExplosionTick(int explosionTick) {
    this.explosionTick = explosionTick;
  }

  public abstract void explode(Level level);

  public abstract int getRadius();

  public abstract int getDelayTicks();
}

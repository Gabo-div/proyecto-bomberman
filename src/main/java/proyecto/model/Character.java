package proyecto.model;

public abstract class Character extends Entity<Double> {
  protected Coord<Double> coord;
  protected int lifes;
  protected boolean dead;
  protected Double speed;

  public Character(Coord<Double> coord, double speed, int lifes) {
    this.coord = coord;
    this.lifes = lifes;
    this.dead = false;
    this.speed = speed;
  }

  @Override
  public Coord<Double> getCoord() {
    return new Coord<>(coord.x, coord.y);
  }

  public void setCoord(Coord<Double> coord) { this.coord = coord; };

  public void setLifes(int lifes) {
    if (lifes <= 0) {
      this.dead = true;
    }

    this.dead = false;
    this.lifes = lifes;
  };

  public int getLifes() { return this.lifes; };

  public boolean isDead() { return this.dead; };

  public void setSpeed(Double speed) { this.speed = speed; };

  public Double getSpeed() { return this.speed; };
}

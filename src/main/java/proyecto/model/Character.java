package proyecto.model;

public abstract class Character extends Entity<Double> {
  protected Coord<Double> coord;
  protected Integer lifes;
  protected Double speed;
  protected Direction direction;
  protected Integer directionState = 1;
  protected Integer directionStateCounter = 0;
  protected Boolean invincible = false;
  protected Integer invincibilityTicks = 0;

  public Character(Coord<Double> coord, double speed, int lifes) {
    this.coord = coord;
    this.lifes = lifes;
    this.speed = speed;
    this.direction = Direction.RIGHT;
  }

  @Override
  public Coord<Double> getCoord() {
    return new Coord<>(coord.x, coord.y);
  }

  public Boolean isInvincible() { return invincible; }

  public void setInvincible(Boolean invincible) {
    this.invincible = invincible;
  }

  public Integer getInvincibilityTicks() { return invincibilityTicks; }

  public void setInvincibilityTicks(Integer invincibilityTicks) {
    this.invincibilityTicks = invincibilityTicks;
  }

  public Direction getDirection() { return direction; }

  public void setDirection(Direction direction) { this.direction = direction; }

  public Integer getDirectionState() { return directionState; }

  public void setDirectionState(Integer directionState) {
    this.directionState = directionState;
  }

  public Integer getDirectionStateCounter() { return directionStateCounter; }

  public void setDirectionStateCounter(Integer directionStateCounter) {
    this.directionStateCounter = directionStateCounter;
  }

  public void setCoord(Coord<Double> coord) { this.coord = coord; };

  public void setLifes(int lifes) { this.lifes = lifes; };

  public Integer getLifes() { return this.lifes; };

  public boolean isDead() { return this.lifes <= 0; };

  public void setSpeed(Double speed) { this.speed = speed; };

  public Double getSpeed() { return this.speed; };
}

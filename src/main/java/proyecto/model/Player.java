package proyecto.model;

public class Player extends Character {
  BombType bombType;
  Integer firepower;

  public Player(Coord<Double> coord, int lifes, Integer firepower) {
    super(coord, 0.25 / 2, lifes);
    this.bombType = BombType.BASIC;
    this.firepower = firepower;
  }

  public Player(double x, double y, int lifes, Integer firepower) {
    super(new Coord<>(x, y), 0.25 / 2, lifes);
    this.bombType = BombType.BASIC;
  }

  public void makeInvincibleUntil(Integer ticks) {
    invincible = true;
    invincibilityTicks = ticks;
  }

  public BombType getBombType() { return bombType; }

  public void setBombType(BombType bombType) { this.bombType = bombType; }

  public Integer getFirepower() { return firepower; }

  public void addFirepower(Integer value) { this.firepower += value; }
}

package proyecto.model;

import proyecto.multiplayer.CharacterColor;

public class Player extends Character {
  BombType bombType = BombType.BASIC;
  Integer firepower;
  Integer availableBombs;
  Integer movementStateX = 0;
  Integer movementStateY = 0;
  CharacterColor color = CharacterColor.WHITE;
  String name;

  public Player(Coord<Double> coord, int lifes, Integer firepower) {
    super(coord, 0.25 / 2, lifes);
    this.bombType = BombType.BASIC;
    this.firepower = firepower;
    this.availableBombs = 1;
  }

  public Player(double x, double y, int lifes, Integer firepower) {
    super(new Coord<>(x, y), 4, lifes);
    this.bombType = BombType.BASIC;
    this.firepower = firepower;
    this.availableBombs = 1;
  }

  public void makeInvincibleUntil(Integer ticks) {
    invincible = true;
    invincibilityTicks = ticks;
  }

  public Integer getAvailableBombs() { return availableBombs; }

  public void setAvailableBombs(Integer availableBombs) {
    this.availableBombs = availableBombs;
  }

  public BombType getBombType() { return bombType; }

  public void setBombType(BombType bombType) { this.bombType = bombType; }

  public Integer getFirepower() { return firepower; }

  public void addFirepower(Integer value) { this.firepower += value; }

  public Integer getMovementStateX() { return movementStateX; }

  public Integer getMovementStateY() { return movementStateY; }

  public void setMovementStateX(Integer movementStateX) {
    this.movementStateX = movementStateX;
  }

  public void setMovementStateY(Integer movementStateY) {
    this.movementStateY = movementStateY;
  }

  public CharacterColor getColor() { return color; }

  public void setColor(CharacterColor color) { this.color = color; }

  public String getName() { return name; }

  public void setName(String name) { this.name = name; }
}

package proyecto.model;

public class Player extends Character {
  BombType bombType;

  public Player(Coord<Double> coord, int lifes) {
    super(coord, 0.25, lifes);
    this.bombType = BombType.BASIC;
  }

  public Player(double x, double y, int lifes) {
    super(new Coord<>(x, y), 0.25, lifes);
    this.bombType = BombType.BASIC;
  }

  public BombType getBombType() { return bombType; }

  public void setBombType(BombType bombType) { this.bombType = bombType; }
}

package proyecto.model;

public abstract class Enemy extends Character {
  public Enemy(Coord<Double> coord, double speed, int lifes) {
    super(coord, speed, lifes);
  }
}

package proyecto.model;

public class Coord<T extends Number> {
  public T x;
  public T y;

  public Coord(T x, T y) {
    this.x = x;
    this.y = y;
  }

  public static Coord<Integer> round(Coord<Double> doubleCoord) {
    Coord<Integer> roundedCoords = new Coord<Integer>(0, 0);

    roundedCoords.x = (int)Math.round(doubleCoord.x);
    roundedCoords.y = (int)Math.round(doubleCoord.y);

    return roundedCoords;
  }
}

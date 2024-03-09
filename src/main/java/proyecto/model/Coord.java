package proyecto.model;

import java.io.Serializable;

/**
 * Esta clase representa una coordenada en el juego, con componentes x e y de un tipo genérico T que extiende a Number.
 * @param <T> El tipo de las componentes de la coordenada.
 */
public class Coord<T extends Number> implements Serializable {
  /**
   * La componente x de la coordenada.
   */
  public T x;
  
  /**
   * La componente y de la coordenada.
   */
  public T y;

  /**
   * Constructor para inicializar la coordenada con valores de x e y.
   * @param x El valor de la componente x.
   * @param y El valor de la componente y.
   */
  public Coord(T x, T y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Método estático para redondear una coordenada de tipo Double a una de tipo Integer.
   * @param doubleCoord La coordenada de tipo Double a redondear.
   * @return La coordenada redondeada de tipo Integer.
   */
  public static Coord<Integer> round(Coord<Double> doubleCoord) {
    Coord<Integer> roundedCoords = new Coord<Integer>(0, 0);

    roundedCoords.x = (int)Math.round(doubleCoord.x);
    roundedCoords.y = (int)Math.round(doubleCoord.y);

    return roundedCoords;
  }
}

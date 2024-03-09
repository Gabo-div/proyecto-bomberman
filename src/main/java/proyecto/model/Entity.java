package proyecto.model;

import java.io.Serializable;

/**
 * Clase abstracta que representa una entidad en el juego.
 * @param <T> Tipo de coordenadas de la entidad.
 */
public abstract class Entity<T extends Number> implements Serializable {

  /**
   * Método abstracto para obtener las coordenadas de la entidad.
   * @return Coordenadas de la entidad.
   */
  public abstract Coord<T> getCoord();
}

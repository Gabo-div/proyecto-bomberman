package proyecto.model;

import java.io.Serializable;

public abstract class Entity<T extends Number> implements Serializable {
  public abstract Coord<T> getCoord();
}

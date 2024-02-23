package proyecto.model;

public enum PowerUpType {
  FIREUP,
  BOMBUP,
  LIFEUP;

  public static PowerUpType random() {
    return values()[(int)(Math.random() * values().length)];
  }
}

package proyecto.model;

public enum Direction {
  UP,
  DOWN,
  LEFT,
  RIGHT;

  public static Direction random() {
    return values()[(int)(Math.random() * values().length)];
  }

  public static Direction randomX() {
    int value = (int)(Math.random() * 2);

    if (value == 0) {
      return LEFT;
    } else {
      return RIGHT;
    }
  }

  public static Direction randomY() {
    int value = (int)(Math.random() * 2);

    if (value == 0) {
      return UP;
    } else {
      return DOWN;
    }
  }

  public Direction getOpposite() {
    switch (this) {
    case UP:
      return DOWN;
    case DOWN:
      return UP;
    case LEFT:
      return RIGHT;
    case RIGHT:
      return LEFT;
    default:
      throw new RuntimeException();
    }
  }
}

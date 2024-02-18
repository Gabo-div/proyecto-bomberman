package proyecto.game;

public class GameConstants {
  static public final int TICK_DURATION_MS = 50;

  static public int secondsToTicks(int seconds) {
    return seconds * 1000 / TICK_DURATION_MS;
  }
}

package proyecto.game;

import javafx.animation.AnimationTimer;

public abstract class GameTimer extends AnimationTimer {
  long lastNano = 0;
  double max = -1;
  double min = -1;
  double media = 0;

  @Override
  public void start() {
    super.start();
    lastNano = 0;
    max = -1;
    min = -1;
    media = 0;
  }

  @Override
  public void handle(long nowNano) {
    if (lastNano == 0) {
      lastNano = nowNano;
      return;
    }

    double deltaMs = (nowNano - lastNano) / 1000000; // nano to mili
    lastNano = nowNano;

    if (deltaMs > max || max == -1)
      max = deltaMs;
    if (deltaMs < min || min == -1)
      min = deltaMs;

    media = (max + min) / 2;

    System.out.println("DELTA: " + deltaMs + " | MAX: " + max +
                       " | MIN: " + min + " | MEDIA: " + media);

    tick(deltaMs);
  }

  public abstract void tick(double deltaMs);
}

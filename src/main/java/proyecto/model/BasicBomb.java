package proyecto.model;

import java.util.List;
import java.util.function.BiFunction;
import proyecto.game.GameConstants;

public class BasicBomb extends Bomb {
  private int explosionDelaySeconds = 5;
  private int radius = 2;

  public BasicBomb(Coord<Integer> coord) { super(coord); }

  @Override
  public int getRadius() {
    return radius;
  }

  @Override
  public int getDelayTicks() {
    return (explosionDelaySeconds * 1000) / GameConstants.TICK_DURATION_MS;
  }

  @Override
  public void explode(Level level, List<Character> characters) {
    exploded = true;

    BiFunction<Integer, Integer, Boolean> explosion =
        (Integer x, Integer y) -> {
      Coord<Integer> eCoord = new Coord<>(this.coord.x + x, this.coord.y + y);

      for (Character character : characters) {
        Coord<Integer> cCoord = Coord.round(character.getCoord());

        if (cCoord.x == eCoord.x && cCoord.y == eCoord.y) {
          character.setLifes(character.getLifes() - 1);
        }
      }

      if (eCoord.x >= level.getWidth()) {
        return true;
      }

      Block block = level.getBlock(eCoord);

      if (block instanceof WallBlock) {
        return false;
      }

      if (block instanceof AirBlock) {
        Entity<Integer> entity = ((AirBlock)block).getEntity();

        if (entity instanceof Bomb) {
          Bomb bomb = (Bomb)entity;

          if (bomb.exploded()) {
            return true;
          }

          bomb.setExplosionTick(explosionTick);
        }
      }

      if (block instanceof BrickBlock) {
        level.setBlock(new AirBlock(eCoord));
      }

      return true;
    };

    for (int x = 0; x <= radius; x++) {
      if (!explosion.apply(x, 0)) {
        break;
      }
    }

    for (int x = 0; x >= -radius; x--) {
      if (!explosion.apply(x, 0)) {
        break;
      }
    }

    for (int y = 0; y <= radius; y++) {
      if (!explosion.apply(0, y)) {
        break;
      }
    }

    for (int y = 0; y >= -radius; y--) {
      if (!explosion.apply(0, y)) {
        break;
      }
    }
  }
}

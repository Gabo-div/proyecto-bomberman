package proyecto.model;

import java.util.List;
import java.util.function.BiFunction;
import proyecto.game.GameConstants;

public class BasicBomb extends Bomb {
  private int explosionDelaySeconds = 3;

  public BasicBomb(Coord<Integer> coord, int firepower) {
    super(coord, firepower);
  }

  @Override
  public int getDelayTicks() {
    return (explosionDelaySeconds * 1000) / GameConstants.TICK_DURATION_MS;
  }

  @Override
  public void explode(Level level) {
    exploded = true;
    List<Character> characters = level.getCharacters();

    BiFunction<Integer, Integer, Boolean> explosion =
        (Integer x, Integer y) -> {
      Coord<Integer> eCoord = new Coord<>(this.coord.x + x, this.coord.y + y);

      for (Character character : characters) {
        if (character.isDead() || character.isInvincible()) {
          continue;
        }

        Coord<Integer> cCoord = Coord.round(character.getCoord());

        if (cCoord.x == eCoord.x && cCoord.y == eCoord.y) {
          character.setLifes(character.getLifes() - 1);

          if (character instanceof Player) {
            Integer invincibilityTicks = GameConstants.secondsToTicks(2);
            ((Player)character)
                .makeInvincibleUntil(explosionTick + invincibilityTicks);
          }
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

    explosion.apply(0, 0);

    for (int x = 1; x <= firepower; x++) {
      if (!explosion.apply(x, 0)) {
        break;
      }
    }

    for (int x = -1; x >= -firepower; x--) {
      if (!explosion.apply(x, 0)) {
        break;
      }
    }

    for (int y = 1; y <= firepower; y++) {
      if (!explosion.apply(0, y)) {
        break;
      }
    }

    for (int y = -1; y >= -firepower; y--) {
      if (!explosion.apply(0, y)) {
        break;
      }
    }
  }
}

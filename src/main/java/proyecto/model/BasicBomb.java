package proyecto.model;

import java.util.List;
import java.util.function.BiFunction;
import proyecto.game.GameConstants;

/**
 * Representa una bomba básica en el juego.
 */
public class BasicBomb extends Bomb {

    /** Retraso de la explosión en segundos. */
    private int explosionDelaySeconds = 3;

    /**
     * Constructor de la bomba básica.
     *
     * @param coord  La coordenada de la bomba.
     * @param player El jugador que colocó la bomba.
     */
    public BasicBomb(Coord<Integer> coord, Player player) {
        super(coord, player);
    }

    /**
     * Obtiene el número de ticks de retraso para la explosión.
     *
     * @return El número de ticks de retraso.
     */
    @Override
    public int getDelayTicks() {
        return (explosionDelaySeconds * 1000) / GameConstants.TICK_DURATION_MS;
    }

    /**
     * Hace que la bomba explote.
     *
     * @param level El nivel en el que se encuentra la bomba.
     */
    @Override
    public void explode(Level level) {
        exploded = true;
        player.setAvailableBombs(player.getAvailableBombs() + 1);

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
                                ((Player) character)
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
                        AirBlock airBlock = (AirBlock) block;
                        Entity<Integer> entity = airBlock.getEntity();

                        if (entity instanceof Bomb) {
                            Bomb bomb = (Bomb) entity;

                            if (bomb.exploded()) {
                                return true;
                            }

                            bomb.setExplosionTick(explosionTick);
                        }

                        if (entity instanceof PowerUp) {
                            airBlock.setEntity(null);
                        }
                    }

                    if (block instanceof BrickBlock) {
                        level.breakBlock(eCoord);
                    }

                    return true;
                };

        explosion.apply(0, 0);

        int firepower = player.getFirepower();

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

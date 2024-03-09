package proyecto.multiplayer.events;

import java.io.Serializable;
import proyecto.model.BombUp;
import proyecto.model.Entity;
import proyecto.model.FireUp;
import proyecto.model.LifeUp;
import proyecto.model.PowerUp;

public class EntityData implements Serializable {
  public String type;

  public EntityData(Entity<Integer> entity) {
    type = "";

    if (!(entity instanceof PowerUp)) {
      return;
    }

    if (entity instanceof FireUp) {
      type = "FIREUP";
    } else if (entity instanceof BombUp) {
      type = "BOMBUP";
    } else if (entity instanceof LifeUp) {
      type = "LIFEUP";
    }
  }
}

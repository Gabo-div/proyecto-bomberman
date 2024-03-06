package proyecto.multiplayer.events;

import java.io.Serializable;
import proyecto.multiplayer.CharacterColor;

public class NewUserData implements Serializable {
  public String name;
  public CharacterColor color;
}

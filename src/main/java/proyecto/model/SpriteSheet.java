package proyecto.model;

import java.io.File;
import java.util.HashMap;

public class SpriteSheet {

  static SpriteSheet instance;

  private HashMap<String, Sprite> spriteMap = new HashMap<>();

  private SpriteSheet() {
    File spritesFolder = new File("src/main/resources/sprites");

    File[] sprites = spritesFolder.listFiles();

    for (int i = 0; i < sprites.length; i++) {
      Sprite sprite = new Sprite(sprites[i]);
      spriteMap.put(sprite.getName(), sprite);
    }
  }

  public static SpriteSheet getInstance() {
    if (instance == null) {
      instance = new SpriteSheet();
    }
    return instance;
  }

  public Sprite getSprite(String name) { return spriteMap.get(name); }
}

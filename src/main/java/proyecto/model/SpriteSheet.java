package proyecto.model;

import java.io.File;
import java.util.HashMap;

/**
 * Clase que representa una hoja de sprites en el juego.
 */
public class SpriteSheet {

  /** Instancia única de SpriteSheet. */
  static SpriteSheet instance;

  /** Mapa de sprites. */
  private HashMap<String, Sprite> spriteMap = new HashMap<>();

  /** Constructor privado de la clase SpriteSheet. */
  private SpriteSheet() {
    File spritesFolder = new File("src/main/resources/sprites");

    File[] sprites = spritesFolder.listFiles();

    if (sprites != null) {
      for (File spriteFile : sprites) {
        try {
          Sprite sprite = new Sprite(spriteFile);
          spriteMap.put(sprite.getName(), sprite);

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Obtiene la instancia única de SpriteSheet.
   * @return La instancia única de SpriteSheet.
   */
  public static SpriteSheet getInstance() {
    if (instance == null) {
      instance = new SpriteSheet();
    }
    return instance;
  }

  /**
   * Obtiene un sprite de la hoja de sprites dado su nombre.
   * @param name El nombre del sprite.
   * @return El sprite correspondiente al nombre dado, o null si no se encuentra.
   */
  public Sprite getSprite(String name) { return spriteMap.get(name); }
}

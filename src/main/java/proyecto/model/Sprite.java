package proyecto.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 * Clase que representa un sprite en el juego.
 */
public class Sprite {

  /** Nombre del sprite. */
  private String name;
  /** Imagen del sprite. */
  private Image image;
  /** Ancho del sprite. */
  private double width;
  /** Altura del sprite. */
  private double height;
  /** Relación de aspecto del sprite. */
  private double ratio;

  /**
   * Constructor de la clase Sprite.
   * @param file Archivo de imagen del sprite.
   * @throws IOException Si ocurre un error de lectura de la imagen.
   */
  public Sprite(File file) throws IOException {
    BufferedImage imageio = ImageIO.read(file);

    name = file.getName().split("\\.")[0];

    width = imageio.getWidth();
    height = imageio.getHeight();
    ratio = width / height;

    image = new Image(file.toURI().toString(), width, height, false, false);
  }

  /**
   * Obtiene el nombre del sprite.
   * @return El nombre del sprite.
   */
  public String getName() { return name; }

  /**
   * Obtiene la imagen del sprite.
   * @return La imagen del sprite.
   */
  public Image getImage() { return image; }

  /**
   * Obtiene la relación de aspecto del sprite.
   * @return La relación de aspecto del sprite.
   */
  public double getRatio() { return ratio; }
}

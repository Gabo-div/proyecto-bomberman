package proyecto.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

public class Sprite {

  private String name;
  private Image image;
  private double width;
  private double height;
  private double ratio;

  public Sprite(File file) throws IOException {
    BufferedImage imageio = ImageIO.read(file);

    name = file.getName().split("\\.")[0];

    width = imageio.getWidth();
    height = imageio.getHeight();
    ratio = width / height;

    image = new Image(file.toURI().toString(), width, height, false, false);
  }

  public String getName() { return name; }

  public Image getImage() { return image; }

  public double getRatio() { return ratio; }
}

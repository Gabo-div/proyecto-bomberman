package proyecto.model;

import java.io.File;
import javafx.scene.image.Image;

public class Sprite {

    private String name;
    private Image image;
    private double width;
    private double height;
    private double ratio;

    public Sprite(File file) {
        name = file.getName().split("\\.")[0];
        image = new Image(file.toURI().toString());
        width = image.getWidth();
        height = image.getHeight();
        ratio = width / height;
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    public double getRatio() {
        return ratio;
    }
}

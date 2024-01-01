package proyecto.bomberman;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.fxml.*;
import javafx.scene.layout.*;
import javafx.scene.canvas.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.*;
import proyecto.model.*;
import proyecto.game.*;

public class SingleplayerController implements Initializable {
    @FXML
    private VBox box;

    private Canvas canvas;
    private GraphicsContext gc;

    private SingleplayerGame game = new SingleplayerGame();

    private double blockSize;
    private double canvasHeight;
    private double canvasWidth;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        URL cssURL = App.class.getResource("singleplayer.css");
        String urlString = cssURL.toString();
        box.getStylesheets().add(urlString);

        Level level = game.getLevel();

        canvasHeight = App.getScene().getHeight();
        blockSize = canvasHeight / level.getHeight();
        canvasWidth = level.getWidth() * blockSize;

        canvas = new Canvas(canvasWidth, canvasHeight);
        gc = canvas.getGraphicsContext2D();
        box.getChildren().add(canvas);
        gc.scale(blockSize, blockSize);

        handleControls();
        initTimer();
    }

    private void handleControls() {
        App.getScene().addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            double speed = 0.25;

            if (e.getCode() == KeyCode.A) {
                movePlayerX(speed * -1);
            } else if (e.getCode() == KeyCode.D) {
                movePlayerX(speed);
            } else if (e.getCode() == KeyCode.W) {
                movePlayerY(speed * -1);
            } else if (e.getCode() == KeyCode.S) {
                movePlayerY(speed);
            } else if (e.getCode() == KeyCode.ENTER) {
                Player player = game.getPlayer();

                Coord<Double> playerCoord = player.getCoord();
                BombType bombType = player.getBombType();

                game.addBomb(bombType, Coord.round(playerCoord));
            }

        });

    }

    public void movePlayerX(double speed) {
        Coord<Double> playerCoord = game.getPlayer().getCoord();
        Coord<Double> newCoord = new Coord<>(playerCoord.x, playerCoord.y);
        Level level = game.getLevel();

        newCoord.x += speed;
        double x = speed > 0 ? Math.ceil(newCoord.x) : Math.floor(newCoord.x);

        if (!level.checkPlayerCollisionX((int) x, newCoord.y)) {
            game.getPlayer().setCoord(newCoord);
        }
    }

    public void movePlayerY(double speed) {
        Coord<Double> playerCoord = game.getPlayer().getCoord();
        Coord<Double> newCoord = new Coord<>(playerCoord.x, playerCoord.y);
        Level level = game.getLevel();

        newCoord.y += speed;
        double y = speed > 0 ? Math.ceil(newCoord.y) : Math.floor(newCoord.y);

        if (!level.checkPlayerCollisionY(newCoord.x, (int) y)) {
            game.getPlayer().setCoord(newCoord);
        }
    }

    private void initTimer() {
        new AnimationTimer() {
            @Override
            public void handle(long l) {
                draw();
            }
        }.start();
    }

    private void draw() {
        SpriteSheet spriteSheet = game.getSpriteSheet();
        Sprite blockSprite = spriteSheet.getSprite("block");
        Sprite brickSprite = spriteSheet.getSprite("brick");

        Coord<Double> playerCoord = game.getPlayer().getCoord();
        ArrayList<Bomb> bombs = game.getBombs();
        Level level = game.getLevel();

        gc.setFill(Color.web("#00ff48"));
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        for (int y = 0; y < level.getHeight(); y++) {
            for (int x = 0; x < level.getWidth(); x++) {
                if (level.getTile(x, y) == 1) {
                    gc.drawImage(blockSprite.getImage(), x, y, 1, 1);
                }

                if (level.getTile(x, y) == 2) {
                    gc.drawImage(brickSprite.getImage(), x, y, 1, 1);
                }

            }
        }

        for (int i = 0; i < bombs.size(); i++) {
            Bomb bomb = bombs.get(i);
            Coord<Integer> bombCoord = bomb.getCoord();

            gc.setFill(Color.RED);
            gc.fillRect(bombCoord.x, bombCoord.y, 1, 1);
        }

        gc.setFill(Color.BLUE);
        gc.fillRect(playerCoord.x, playerCoord.y, 1, 1);
    }
}

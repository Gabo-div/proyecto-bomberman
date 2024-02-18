package proyecto.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class KeyHandler {
  private static Scene scene;
  private static final Set<KeyCode> keysCurrentlyDown = new HashSet<>();
  private static final Map<KeyCode, ArrayList<Runnable>> keyHandlers =
      new HashMap<>();

  private static KeyHandler instance;

  private KeyHandler() {}

  public static KeyHandler getInstance() {
    if (instance == null) {
      instance = new KeyHandler();
    }

    return instance;
  }

  public void pollScene(Scene scene) {
    clearKeys();
    clearHandlers();
    removeCurrentKeyHandlers();
    setScene(scene);
  }

  private void clearKeys() { keysCurrentlyDown.clear(); }

  private void clearHandlers() { keyHandlers.clear(); }

  private void removeCurrentKeyHandlers() {
    if (scene != null) {
      KeyHandler.scene.setOnKeyPressed(null);
      KeyHandler.scene.setOnKeyReleased(null);
    }
  }

  private void setScene(Scene scene) {
    KeyHandler.scene = scene;
    KeyHandler.scene.setOnKeyPressed((keyEvent -> {
      keysCurrentlyDown.add(keyEvent.getCode());

      ArrayList<Runnable> handlers = keyHandlers.get(keyEvent.getCode());

      if (handlers == null) {
        return;
      }

      for (Runnable handler : handlers) {
        handler.run();
      }
    }));

    KeyHandler.scene.setOnKeyReleased(
        (keyEvent -> { keysCurrentlyDown.remove(keyEvent.getCode()); }));
  }

  public boolean isDown(KeyCode keyCode) {
    return keysCurrentlyDown.contains(keyCode);
  }

  public void onPressed(KeyCode keyCode, Runnable runnable) {
    keyHandlers.putIfAbsent(keyCode, new ArrayList<>());
    keyHandlers.get(keyCode).add(runnable);
  }
}

module proyecto.bomberman {
  requires javafx.controls;
  requires transitive javafx.fxml;
  requires transitive javafx.graphics;
  requires javafx.base;
  requires java.desktop;

  opens proyecto.bomberman to javafx.fxml;

  exports proyecto.bomberman;
}

module proyecto.bomberman {
  requires javafx.controls;
  requires javafx.fxml;
  requires transitive javafx.graphics;
  requires javafx.base;

  opens proyecto.bomberman to javafx.fxml;

  exports proyecto.bomberman;
}

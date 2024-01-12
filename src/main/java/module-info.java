module proyecto.bomberman {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens proyecto.bomberman to javafx.fxml;

    exports proyecto.bomberman;
}

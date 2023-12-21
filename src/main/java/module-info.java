module proyecto.bomberman {
    requires javafx.controls;
    requires javafx.fxml;

    opens proyecto.bomberman to javafx.fxml;

    exports proyecto.bomberman;
}

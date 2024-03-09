/**
 * Este módulo define las dependencias y la estructura de paquetes para el proyecto Bomberman.
 * Requiere JavaFX para los controles de interfaz gráfica, el manejo de archivos FXML y la representación gráfica.
 * También requiere JavaFX para las funcionalidades básicas y Java Desktop para ciertas funcionalidades de escritorio.
 * Abre el paquete 'proyecto.bomberman' para el uso de archivos FXML y exporta el paquete 'proyecto.bomberman'
 * para su uso por otros módulos.
 */
module proyecto.bomberman {
  requires javafx.controls;
  requires transitive javafx.fxml;
  requires transitive javafx.graphics;
  requires javafx.base;
  requires java.desktop;

  opens proyecto.bomberman to javafx.fxml;
  exports proyecto.bomberman;
}

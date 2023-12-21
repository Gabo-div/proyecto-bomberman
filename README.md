# Bomberman

Proyecto de Bomberman hecho en Java para la universidad

## Instalación y Ejecución

Instala todas las dependiencias con Maven:

```bash
 mvn clean install
```

Ejecuta el siguiente comando para generar una imagen de ejecución:

```bash
 mvn clean javafx:jlink
```

Luego ejecuta uno de los siguientes archivos dependiento de tu sistema operativo para ejecutar el programar desde la imagen creada:

En Windows:

```bash
 target\bomberman\bin\launcher.bat
```

En Linux:

```bash
 target/bomberman/bin/launcher
```

package proyecto.bomberman;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.layout.*;

public class AboutController implements Initializable {
    @FXML private VBox box;
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      URL cssURL = App.class.getResource("about.css");
      String urlString = cssURL.toString();
      box.getStylesheets().add(urlString);
    }

    @FXML
  private void switchToPrimary() throws IOException {
    App.setRoot("primary");
  }

  
    @FXML
    public void Exit() {
      App.getStage().close();
      System.exit(0);
    }
  }

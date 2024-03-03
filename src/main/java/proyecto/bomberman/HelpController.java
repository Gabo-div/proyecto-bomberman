package proyecto.bomberman;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.layout.*;

public class HelpController implements Initializable {
    @FXML private VBox box;
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      URL cssURL = App.class.getResource("help.css");
      String urlString = cssURL.toString();
      box.getStylesheets().add(urlString);
    }
  
    @FXML
    public void Exit() {
      App.getStage().close();
      System.exit(0);
    }
  }

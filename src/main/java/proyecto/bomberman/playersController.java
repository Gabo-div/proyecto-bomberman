package proyecto.bomberman;

import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;


import Multiplayer.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


//Clase que coordina quien envía y recibe mensajes
public class playersController implements Initializable{

    @FXML private VBox box;
    @FXML private VBox chat;
    @FXML private VBox room;

    @FXML
    private Button button_send;

    @FXML
    private TextField tf_message;

    @FXML
    private VBox vbox_messages;

    @FXML
    private ScrollPane sp_main; 
    
    @FXML
    private static GridPane gridpane;
    
    @FXML
    private static TextField tf_username;

    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        URL cssURL = App.class.getResource("joinlobby.css");
        String urlString = cssURL.toString();
        box.getStylesheets().add(urlString);
        box.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        room.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        

        try {
        	
            this.client = new Client(new Socket("localhost", 5000));
            System.out.println("Se ha conectado el cliente "+ client);
        } catch (Exception e) {
            System.out.println("No se ha encontrado partida disponible");
            e.printStackTrace();
        }

        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {    

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });

        
        client.receiveMessageFromServer(vbox_messages);
        
       
        


        button_send.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String messageToSend = tf_message.getText();
                if (!messageToSend.isEmpty()){
                    HBox hbox = new HBox();
                    hbox.setAlignment(Pos.TOP_RIGHT);
                    hbox.setPadding(new Insets(5,5,5,10));

                    Text text = new Text(messageToSend);
                    TextFlow textFlow = new TextFlow(text);

                     textFlow.setStyle("-fx-color: rgb(259,240,200);"+
                            " -fx-background-color: rgb(50, 50, 50);"+
                            " -fx-background-radius: 20px;"+
                            " -fx-padding: 5;");

                    textFlow.setPadding(new Insets(5,10,5,10));
                    text.setFill(Color.color(0.934,0.945,0.996));

                    hbox.getChildren().add(textFlow);
                    vbox_messages.getChildren().add(hbox);

                    client.sendMessageToServer(messageToSend);
                    tf_message.clear();
            }
            
        }});
            
    }
    
    

    public static void addLabel(String messageFromServer, VBox vbox){

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.TOP_LEFT);
        hbox.setPadding(new Insets(5,5,5,10));

        Text text = new Text(messageFromServer);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle(" -fx-background-color: rgb(233, 233, 235);" +
                " -fx-background-radius: 20px;");

        textFlow.setPadding(new Insets(5,10,5,10));
        hbox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox.getChildren().add(hbox);
            }
        });
    }

}

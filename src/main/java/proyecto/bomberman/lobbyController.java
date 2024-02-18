package proyecto.bomberman;

import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

import Multiplayer.Server;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.HBox;
import javafx.beans.value.ObservableValue;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;

public class lobbyController implements Initializable{

    @FXML
    private TextField tf_message;
    @FXML 
    private VBox vbox_messages;
    @FXML
    private ScrollPane sp_main;
    @FXML
    private AnchorPane ap_chat;
    @FXML
    private Button button_send;
    

    private Server server;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
        try {
            //server = new Server(new ServerSocket(5000));
    
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al iniciar el servidor");
        }

        //Actualiza el scrollpane de los mensajes
        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });


        server.receiveMessageFromClient(vbox_messages);

        
        button_send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String messageToSend = tf_message.getText();
                if (!messageToSend.isEmpty()){
                    HBox hbox = new HBox();
                    hbox.setAlignment(Pos.CENTER_RIGHT);
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

                    server.sendMessageToClient(messageToSend);
                    tf_message.clear();
                }
            }
        });
    }

    public static void addLabel(String messageFromClient, VBox vbox){

        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5,5,5,10));

        Text text = new Text(messageFromClient);
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

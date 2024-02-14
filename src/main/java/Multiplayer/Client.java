package Multiplayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javafx.scene.layout.VBox;
import proyecto.bomberman.playersController;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private int clientPlayerCount;

    public Client(Socket socket){
        
        try {
        
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        //Server.playerCount++;
        //clientPlayerCount = Server.playerCount;
        
        

        } catch (Exception e) {
            System.out.println("Error al crear el cliente");
            e.printStackTrace();
            logout(socket, bufferedReader, bufferedWriter);
        }
        

        
    }
    

    public void sendMessageToServer(String messageToServer){
        try {

            bufferedWriter.write(messageToServer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            
        }catch(IOException e){
            System.out.println("Error al enviar el mensaje al servidor");
            e.printStackTrace();
            logout(socket, bufferedReader, bufferedWriter);
        }
    }

    public void receiveMessageFromServer(VBox vbox){
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (socket.isConnected()){
                    try {

                        String messageFromServer = bufferedReader.readLine();
                        
                        
                        playersController.addLabel(messageFromServer, vbox);

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("Error al recibir el mensaje del cliente");
                        logout(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                    
                }
            }
           
            
        }).start();
        
    }
    

    public void logout(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}

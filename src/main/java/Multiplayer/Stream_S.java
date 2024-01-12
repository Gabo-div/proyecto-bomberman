package Multiplayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Stream_S {
    
    private int maxPlayers = 4;


    Stream_S(String PORT, String IPaddress){

        
    }

    private void StartServer(){

        try {
            ServerSocket serverSocket = new ServerSocket(9001);


            while(true){

                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
            

        } catch (IOException e) {
            
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket){

        try {
            ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());

            while(true){
                try {
                    String message = (String) input.readObject();

                    output.writeObject("Server received " + message);

                } catch (ClassNotFoundException e) {
                    
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
           
            e.printStackTrace();
        }

    }

    
}

package Multiplayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import proyecto.bomberman.lobbyController;
import proyecto.bomberman.playersController;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    private int position;
    //private static int playerCount = 1;

    public ClientHandler(Socket socket){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Server.playerCount++;
            this.clientUsername = "Player " + Server.playerCount;
            clientHandlers.add(this);
            broadcastServerMessage("SERVER: " + this.clientUsername + " has entered the chat");
            
            //int position = Server.playerCount - 1;
            //broadcastMessagePosition("POSITION:" + position);



        }catch (Exception e) {
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }
    
    public int UserGridPosition(){
	    
    	try {
			for(ClientHandler clientHandler : clientHandlers){
				if(clientHandler.clientUsername.equals(clientUsername)){
					
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return position;
    }
    
    

    private void closeEverything(Socket socket2, BufferedReader bufferedReader2, BufferedWriter bufferedWriter2) {
        
        removeClientHandler();
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch(IOException e){
            e.printStackTrace();
        }

    }

    
    private void broadcastMessage(String string) {
        for(ClientHandler clientHandler : clientHandlers){
            try{
                if(!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(clientUsername + ": " + string);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();

                    
                }
            } catch(IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
            }   
        }
        
    }
    
    private void broadcastServerMessage(String string) {
        for(ClientHandler clientHandler : clientHandlers){
            try{
                if(!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(string);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();

                    
                }
            } catch(IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
            }   
        }
        
    }

    private void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left the chat");
    }

    @Override
    public void run() {
        String messageFromClient;

        while(socket.isConnected()){
            try{
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            }catch(IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
                break;
            }
        }
    }
    
}

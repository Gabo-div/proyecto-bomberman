package Multiplayer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.scene.layout.VBox;
import proyecto.bomberman.lobbyController;
import proyecto.bomberman.playersController;

//Create a server that uses Hamachi for multiplayer
public class Server{
    
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public static int playerCount = 0;


    public Server(ServerSocket serverSocket){
        
        this.serverSocket = serverSocket;
        System.err.println("hola");
      
        /* 
        try {
            this.serverSocket = serverSocket;
            this.socket = serverSocket.accept();
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Error al crear el servidor");
            e.printStackTrace();
            closeServer(socket, bufferedReader, bufferedWriter);
        }
        */
    }

    public void startServer() throws IOException{

                
            while (true) {
                Socket socket = serverSocket.accept();
                System.err.println("Se ha conectado un nuevo cliente");      
                ClientHandler clientHandler = new ClientHandler(socket); 
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
                
        }
            
      
    

    public void receiveMessageFromClient(VBox vbox){
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true){
                    try {

                        String messageFromClient = bufferedReader.readLine();
                        playersController.addLabel(messageFromClient, vbox);

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("Error al recibir el mensaje del cliente");
                        closeServer(socket, bufferedReader, bufferedWriter);
                    }
                    
                }
            }
           
            
        }).start();
    }

    public void sendMessageToClient(String messageToClient){
        try {
            bufferedWriter.write(messageToClient);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
        catch(IOException e){
            System.err.println("Error al enviar mensaje al cliente");
            e.printStackTrace();
            closeServer(socket, bufferedReader, bufferedWriter);
        }
    }

    public void closeServer(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
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


        } catch(IOException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        Server server = new Server(serverSocket);
        server.startServer();
    }


    //read data: socket is input stream
    //send data: socket is output stream

    //how a connection is stablished between client and server with sockets?
    //socket at both ends (client and server)
    //the client already has a socket but the server has a serverSocket

    //server Socket: waits for connections from clients
    //and once its stablished, a socket will be created on the server side
    
    
    
}       

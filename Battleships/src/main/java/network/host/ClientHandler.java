package network.host;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class is responsible for handling I/O between server and client from the server's perspective.
 * Implements a listener for messages from one specific client and relays them to both clients.
 */
public class ClientHandler extends Thread{
    Socket client;
    /** Each ClientHandler contains the list of all ClientHandlers, so they can send messages to all clients.*/
    ArrayList<ClientHandler> threads;
    BufferedReader in;
    PrintWriter out;

    /**
     * Constructor for Clienthandler (to be called by the Server's constructor when clients connect to it).
     * One ClientHandler per client listens to incoming messages from that client via the run() method.
     * @param clientSocket
     * @param threads
     * @throws IOException
     */
    public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> threads) throws IOException {
        this.client = clientSocket;
        this.threads = threads;
        //an input stream to receive Strings from the client
        this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        //an output stream to send Strings to the client via print methods (similar to printstream)
        //autoFlush makes sure the messages actually get sent. Normally they only get sent once the PrintWriter buffer is full or it's told to flush manually
        this.out = new PrintWriter(client.getOutputStream(), true);
    }

    /**
     * A listener for incoming messages from the client.
     * They are automatically sent to both clients.
     */
    @Override
    public void run(){
        try {
            while(true) {
                String messageFromClient = this.in.readLine();
                threads.get(0).sendMessageToClient(messageFromClient);
                threads.get(1).sendMessageToClient(messageFromClient);
            }
        }catch (IOException e) {
            System.exit(0);
        }
    }

    /**
     * Sends a message to the client this ClientHandler is responsible for.
     * @param message
     */
    public void sendMessageToClient(String message){
        out.println(message);
    }
}

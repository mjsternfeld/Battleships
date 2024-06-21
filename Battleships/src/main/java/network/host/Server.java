package network.host;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

/**
 * This class initializes the Server. It contains the clients which are connected to it,
 * ClientHandlers which are responsible for the clients, and methods to send String
 * messages to either or both of the clients.
 */
public class Server{
    ServerSocket listener;
    ArrayList<ClientHandler> threads;
    Socket[] clients;

    /**
     * Server constructor. Makes the server available to connect to and waits for two clients to connect,
     * then starts the Listeners' run methods (on separate threads).
     * @throws IOException
     */
    public Server() throws IOException {
        //port is opened
        /* the server (the machine this is running on) makes itself available
         * to connect to at that port / listens to incoming connections
         */
        this.listener = new ServerSocket(9999);

        //clients connect
        //the machine that connects to the server at that port is saved as Socket "client" in the network.host.Server class
        //threads contains the ClientHandlers, ie. the objects that listen to incoming messages / send messages to the clients
        threads = new ArrayList<>();
        int numClients = 0;
        clients = new Socket[2];

        while(numClients < 2){
            Socket client = listener.accept();
            threads.add(new ClientHandler(client, threads));
            numClients++;
        }
        //server starts listening to both clients on separate threads
        threads.get(0).start();
        threads.get(1).start();
    }

    /**
     * Method to close the established connections (kinda redundant, but might be useful later)
     * @throws IOException
     */
    public void closeConnections() throws IOException {
        this.listener.close();
        this.clients[0].close();
        this.clients[1].close();
    }

    /**
     * Sends a String message to both clients. The message can be either a chat message, or any of the objects we want to transmit (this is irrelevant to this method).
     * @param message
     */
    public void sendMessageToClients(String message){
        sendMessageToClient1(message);
        sendMessageToClient2(message);
    }

    /**
     * Sends a String message to client1 (the host client). The message can be either a chat message, or any of the objects we want to transmit (this is irrelevant to this method).
     * @param message
     */
    public void sendMessageToClient1(String message){
        this.threads.get(0).sendMessageToClient(message);
    }

    /**
     * Sends a String message to client2 (the "client" client). The message can be either a chat message, or any of the objects we want to transmit (this is irrelevant to this method).
     * @param message
     */
    public void sendMessageToClient2(String message){
        this.threads.get(1).sendMessageToClient(message);
    }


}
package network;

import gui.ClientChat;
import gui.HostChat;

import java.io.IOException;
import java.net.*;

/**
 * Client class, used to connect to a server and establish that connection.
 * Provides methods to send messages and Objects to the server, as well as update
 * the chat GUI.
 */
public class Client{
    boolean isHost;
    String name;
    String serverIP;
    int serverPort;
    /** The server to which the client connects to, represented as a Socket object. */
    public Socket s;
    /** The ServerConnection object assigned to receive messages from the server or send messages to it */
    public ServerConnection sc;

    /**
     * Client constructor. To be called by Player2, ie. the player who doesn't host the game.
     * @param serverIP
     * @param name
     * @param serverPort
     * @throws IOException
     */
    public Client(String serverIP, String name, int serverPort) throws IOException {
        this.name = name;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.isHost = false;

        s = new Socket(serverIP, serverPort);
        sc = new ServerConnection(s, this);
        sc.start();
    }

    /**
     * Client constructor. To be called by client1 aka host. Since the host client always connects to localhost (127.0.0.1), less parameters are needed.
     * @param name The player's name
     * @throws IOException
     */
    public Client(String name) throws IOException{
        this.name = name;
        this.serverIP = "127.0.0.1";
        this.serverPort = 9999;
        this.isHost = true;
        try {Thread.sleep(5000);} catch (InterruptedException e) {throw new RuntimeException(e);}
        s = new Socket(serverIP, serverPort);
        sc = new ServerConnection(s, this);
        sc.start();
    }

    /**
     * Sends a message to the server (who then sends it back to both clients). Adds the player's (sender's) name to it in front
     * @param message
     */
    public void sendMessageToServer(String message){
        this.sc.out.println("[" + this.name + "]: " + message);
    }

    /**
     * Sends a shipArray String to the server (Map.createShipArrayString()) (as a way to describe the map)
     * Adds the "[SHIPARRAY]" keyword to it so the receiver recognizes this as a shipArray, rather than a chat message.
     * @param shipArray
     */
    public void sendShipArrayToServer(String shipArray){
        if(this.isHost){
            this.sc.out.println("[HOSTSHIPARRAY]" + shipArray);
        }else{
            this.sc.out.println("[CLIENTSHIPARRAY]" + shipArray);
        }
    }

    /**
     * Sends a Point / coordinates to the server.
     * Adds the "[POINT]" keyword to it so the receiver recognizes this as a Point, rather than a chat message.
     * @param x
     * @param y
     */
    public void sendCoordinatesToServer(int x, int y){
        if(this.isHost){
            this.sc.out.println("[HOSTPOINT]" + x + "," + y);
        }else{
            this.sc.out.println("[CLIENTPOINT]" + x + "," + y);
        }
    }

    /**
     * Takes a string and appends it to the respective player's Chat GUI. To be called by the ServerConnection listener.
     * @param message
     */
    public void updateChat(String message) {
        if(this.isHost)
            HostChat.messages.appendText(message);
        else
            ClientChat.messages.appendText(message);
    }

    public void sendTurnMessageToServer(String s) {
        if(this.isHost)
            this.sc.out.println("[TURN]: " + s);
    }
}

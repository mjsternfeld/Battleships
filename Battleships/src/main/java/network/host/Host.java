package network.host;

import network.Client;
import java.io.IOException;
import java.net.*;

/**
 * This class is used by player 1's game.
 * The "Host" button should create an instance of Host.
 * Messages can be sent from client to server or vice versa via the sendMessage[...]() methods
 * The entire chat history can be accessed via getChatHistory()
 * You can also send messages as the server to the players.
 */
public class Host extends Thread{
    /** Player 1's OWN client (not to be confused with the second player's client) */
    static Client c;
    public static Server s;
    /**
     * Constructor for host, to be called by the Host button in the GUI.
     * Starts the run method (which iniializes the Server object), then initializes a Client which connects to it.
     * @param playerName
     * @throws IOException
     */
    public Host(String playerName) throws IOException {
        this.start(); //creates a server on a separate thread
        c = new Client(playerName); //creates the client
    }

    public Client getClient(){
        return this.c;
    }
    public void sendMessageFromClientToServer(String message){
        c.sendMessageToServer(message);
    }

    public String getChatHistory(){
        return c.sc.getChatHistory();
    }
    /**
     * Creates a Server object on a separate thread.
     */
    @Override
    public void run() {
        try {s = new Server();} catch (IOException e) {throw new RuntimeException(e);}
    }

    public static String getIPAddress(){
        try (final DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.connect(InetAddress.getByName("8.8.8.8"), 12345);
            String ip = datagramSocket.getLocalAddress().getHostAddress();
            return ip;
        } catch (UnknownHostException | SocketException e) {
            throw new RuntimeException(e);
        }
    }

}

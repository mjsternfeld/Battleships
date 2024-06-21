package network;

import game.assets.Map;
import gui.StartGUI;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import network.host.Host;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class handles (String) i/o between server and client from the client's perspective.
 * Implements a listener in its run() method, to run on a separate thread, which receives and evaluates
 * String messages received from the server.
 */
public class ServerConnection extends Thread{
    Socket server;
    Client c;
    //objects needed for i/o
    BufferedReader in;
    public PrintWriter out;
    String chatHistory;
    /**
     * Constructor for ServerConnection. Creates all the necessary objects for sending and receiving Strings to and from the server.
     * @param s
     * @param c
     * @throws IOException
     */
    public ServerConnection(Socket s, Client c) throws IOException {
        this.server = s;
        this.c = c;
        this.chatHistory = "";
        this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.out = new PrintWriter(s.getOutputStream(), true);
    }
    /**
     * A listener which receives and evaluates Strings received from the Server
     */
    @Override
    public void run() {
        try {
            while (true) {
                String serverMessage = in.readLine();
                //this.addMessageToChatHistory(serverMessage);
                //System.out.println(this.getChatHistory());
                if(serverMessage != null) {
                    if (serverMessage.contains("[HOSTPOINT]") && !this.c.isHost) {
                        //the host sends coordinates, that means the host bombs the clientplayer's map
                        int x = Integer.parseInt(serverMessage.substring(11, serverMessage.indexOf(",") ));
                        int y = Integer.parseInt(serverMessage.substring(serverMessage.indexOf(",")+1));
                        StartGUI.getHostPlayer().attackTile(x+1,y+1);
                        StartGUI.getClientPlayer().receiveAttack(x+1,y+1);
                        StartGUI.getHostGameboardGUI().refreshBoards();
                        StartGUI.getClientGameboardGUI().refreshBoards();
                    }
                    else if (serverMessage.contains("[HOSTPOINT]") && this.c.isHost) {
                        //
                    } else if (serverMessage.contains("[HOSTSHIPARRAY]") && !this.c.isHost) {
                        StartGUI.getClientPlayer().updateOpponentMap(serverMessage.substring(15));
                    } else if (serverMessage.contains("[HOSTSHIPARRAY]") && this.c.isHost) {
                        //this is only here so the message doesn't end up in the chat
                    } else if (serverMessage.contains("[CLIENTPOINT]") && this.c.isHost) {
                        //the client sends coordinates, that means the clientplayer bombs the hostplayer's map
                        int x = Integer.parseInt(serverMessage.substring(13, serverMessage.indexOf(",") ));
                        int y = Integer.parseInt(serverMessage.substring(serverMessage.indexOf(",")+1));
                        StartGUI.getHostPlayer().receiveAttack(x+1,y+1);
                        StartGUI.getClientPlayer().attackTile(x+1,y+1);
                        StartGUI.getHostGameboardGUI().refreshBoards();
                        StartGUI.getClientGameboardGUI().refreshBoards();
                    }
                    else if (serverMessage.contains("[CLIENTPOINT]") && !this.c.isHost){

                    } else if (serverMessage.contains("[CLIENTSHIPARRAY]") && this.c.isHost) {
                        StartGUI.getHostPlayer().updateOpponentMap(serverMessage.substring(17));
                    }else if (serverMessage.contains("[CLIENTSHIPARRAY]") && !this.c.isHost) {
                        //this is only here so the message doesn't end up in the chat
                    }else if(serverMessage.contains("[HOSTTURN]")) { //called when the client hits a water tile
                        StartGUI.getHostPlayer().setMyTurn(true);
                        StartGUI.getClientPlayer().setMyTurn(false);
                        this.c.updateChat("It's player 1's (the host's) turn!\n");
                    }else if(serverMessage.contains("[CLIENTTURN]")) { //called when the host hits a water tile
                        StartGUI.getHostPlayer().setMyTurn(false);
                        StartGUI.getClientPlayer().setMyTurn(true);
                        this.c.updateChat("It's player 2's (the client's) turn!\n");
                    }else {
                        this.c.updateChat(serverMessage + "\n");
                    }
                }
            }
        } catch (IOException e) {
            System.exit(0);
        }
    }
    public String getChatHistory(){
        return this.chatHistory;
    }

}

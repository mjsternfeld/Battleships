package network.client;

import network.Client;
import java.io.IOException;

/*
This class is used by player 2's game.
The "Join" button should create a Join object with the required parameters.
Messages can be sent via sendMessage()
The entire chat history can be accessed via getChatHistory()
(you could try something like an infinite loop that constantly sets
the GUI text field to getChatHistory() but i'm not sure how that works )
*/
public class Join{

    static Client c;

    public Join(String serverIP, String playerName) throws IOException {
        c = new Client(serverIP, playerName, 9999);
    }

    public Client getClient(){
        return c;
    }
    public void sendMessage(String message){
        c.sendMessageToServer(message);
    }

    public static String getChatHistory(){
        return c.sc.getChatHistory();
    }

}

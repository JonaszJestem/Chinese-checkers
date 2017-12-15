package Game;

import Client.Client;
import javafx.fxml.FXML;

import java.io.IOException;

//TODO yo dude whatta fuk, whass good
public class GameGUIController {
    @FXML
    private Client client;

    public void shutdown() {
        try {
            if (client.gameSocket != null) {
                client.gameSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

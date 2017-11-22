import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;


public class GUIController {
    @FXML
    private Client client;
    @FXML
    private AnchorPane loginScreen;
    @FXML
    private TextField username;
    @FXML
    private Pane connectionError;
    @FXML
    private AnchorPane gamesList;
    @FXML
    private ListView<String> gameList = new ListView<>();


    private ObservableList<String> games = FXCollections.observableArrayList();

    @FXML
    protected void handleLoginButton() {
        client = new Client();
        loginScreen.setVisible(false);
        client.setUserName(username.getText());
        gameList.setItems(games);
        try {
            client.connect();
            handleGettingGames();
            gamesList.setVisible(true);
        } catch (IOException e) {
            connectionError.setVisible(true);
        }
    }

    @FXML
    protected void handleGettingGames() {
        games.clear();

        for (Game g : client.getGamesFromServer()) {
            games.add(g.getName());
        }
    }

    //TODO: Implement making new game properties (JSON) and send to server and refresh games
    private void handleNewGame() {

    }
}

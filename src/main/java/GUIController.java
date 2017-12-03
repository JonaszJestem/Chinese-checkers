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
    @FXML
    private ListView<Integer> possiblePlayers = new ListView<>();
    @FXML
    private AnchorPane gameCreator;
    @FXML
    private TextField nameOfGame;

    private ObservableList<Integer> possiblePlayersItems = FXCollections.observableArrayList(2,3,4,6);

    @FXML
    protected void handleLoginButton() {
        //Create new Client
        client = new Client();
        gameList.setItems(client.games);

        //Set username and disable name screen
        client.setUserName(username.getText());
        loginScreen.setVisible(false);

        //Connect to server and fetch games list from server
        try {
            client.connect();
            handleGettingGames();
            System.out.println(client.games);
            gamesList.setVisible(true);
        } catch (IOException e) {
            connectionError.setVisible(true);
        }
    }

    @FXML
    protected void handleGettingGames() {
        client.getGamesFromServer();
    }

    @FXML
    private void handleNewGame() {
        gamesList.setVisible(false);
        possiblePlayers.setItems(possiblePlayersItems);
        gameCreator.setVisible(true);
    }

    @FXML
    private void handleCreateGameButton() {
        client.addGame(nameOfGame.getText(), possiblePlayers.getSelectionModel().getSelectedItem());
        gameCreator.setVisible(false);
        gamesList.setVisible(true);
    }

    @FXML
    private void handleCancelButton() {
        gameCreator.setVisible(false);
        gamesList.setVisible(true);
    }
}

package Client;

import Game.GameGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;


public class GUIController {
    @FXML
    private Client client = new Client(); //TODO ok???
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
    @FXML
    private Text greeting;

    private ObservableList<Integer> possiblePlayersItems = FXCollections.observableArrayList(2,3,4,6);
    private ObservableList<String> games = FXCollections.observableArrayList();


    //TODO Implement closing GUI before login when client isn't instantiated yet (???) SEE: declaration of Client variable
    public void shutdown() {
        try {
            if (client.socket != null) {
                if (client.isConnected)
                    client.disconnect();
                client.socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleLoginButton() {
        //Create new Client.Client
        //client = new Client();
        //Set username and disable name screen
        if (username.getText().equals(""))
            client.setUserName("Player");
        else
            client.setUserName(username.getText());

        greeting.setText("Hello, " + client.getUserName());
        loginScreen.setVisible(false);

        //Connect to server and fetch games list from server
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
        client.getGamesFromServer();
        games = FXCollections.observableArrayList(client.getGames());
        gameList.setItems(games);
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
        handleGettingGames();
        gamesList.setVisible(true);
    }

    @FXML
    private void handleCancelButton() {
        gameCreator.setVisible(false);
        gamesList.setVisible(true);
    }

    @FXML
    private void handleJoinGame() {
        if (gameList.getItems().size() == 0) return;
        try{
            int gameID = Integer.parseInt(gameList.getSelectionModel().getSelectedItem().split(" ")[0]);
            client.joinGame(gameID);
        }catch(NullPointerException ex){
            return;
        }


        client.gameGUI = new GameGUI(client);
    }
}

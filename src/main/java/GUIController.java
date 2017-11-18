import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    protected void handleLoginButton(ActionEvent event) {
        client = new Client();
        loginScreen.setVisible(false);
        client.setUserName(username.getText());
        System.out.println(client.getUserName());
        try {
            client.connect();
        } catch (IOException e) {
            connectionError.setVisible(true);
        }
    }
}

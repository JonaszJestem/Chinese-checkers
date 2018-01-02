package Client;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

import java.io.IOException;

public class ClientGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientGUI.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("Chinese checkers");
        primaryStage.setScene(scene);

        primaryStage.setResizable(false);

        GUIController controller = loader.getController();
        primaryStage.setOnCloseRequest(e -> controller.shutdown());

        primaryStage.show();
    }
}

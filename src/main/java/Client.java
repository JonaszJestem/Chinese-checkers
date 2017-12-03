import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class Client {
    private final int port = 8000;
    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;

    private String userName;
    ObservableList<String> games = FXCollections.observableArrayList();
    ArrayList<Game> newGames = new ArrayList<>();
    Gson JSONParser = new Gson();
    String line;

    void setUserName(String userName) {
        this.userName = userName;
    }

    void connect() throws IOException {
        socket = new Socket("localhost", port);

        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printWriter = new PrintWriter(socket.getOutputStream(), true);
    }

    void getGamesFromServer() {
        try {
            bufferedReader.ready();
            printWriter.println("GETGAMES");
            while ((line = bufferedReader.readLine()) != null) {
                if(!newGames.isEmpty()) newGames.clear();
                newGames.addAll(JSONParser.fromJson(line, new TypeToken<ArrayList<Game>>() {}.getType()));
                this.games.clear();
                for (Game g : newGames) {
                    games.add(g.getName());
                }
            }
            } catch(IOException e){
                e.printStackTrace();
            }
    }

    void addGame(String text, int possiblePlayers) {
        if(text.equalsIgnoreCase("")) text = "newGame";
        printWriter.println("CREATEGAME " + text.replaceAll("\\s+", "") + " " + possiblePlayers);
        printWriter.flush();
        return;
    }
}

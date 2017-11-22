import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
    private List<Game> games = new ArrayList<>();


    void setUserName(String userName) {
        this.userName = userName;
    }

    void connect() throws IOException {
        socket = new Socket("localhost", port);

        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printWriter = new PrintWriter(socket.getOutputStream(), true);
    }

    List<Game> getGamesFromServer() {
        try {
            bufferedReader.ready();
            printWriter.println("GETGAMES");
            String games = bufferedReader.readLine();

            ArrayList<Game> newGames = new Gson().fromJson(games, new TypeToken<ArrayList<Game>>() {
            }.getType());
            this.games.clear();
            if (newGames != null) {
                this.games.addAll(newGames);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return games;
    }
}

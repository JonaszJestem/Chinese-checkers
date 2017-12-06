package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    private final int port = 8000;
    static ArrayList<String> games = new ArrayList<>();
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    public boolean isConnected = false;

    private String userName;
    Socket socket;
    private String line;

    void setUserName(String userName) {
        this.userName = userName;
    }

    public void connect() throws IOException {
        if (isConnected) return;
        socket = new Socket("localhost", port);

        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printWriter = new PrintWriter(socket.getOutputStream(), true);
        isConnected = true;
    }

    public void getGamesFromServer() {
        try {
            bufferedReader.ready();
            printWriter.println("GETGAMES");
            line = bufferedReader.readLine();
            parseGamesFromServer(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseGamesFromServer(String line) {
        games.clear();
        if (line.length() == 0) return;
        String[] gameList = line.split(";");
        for (String g : gameList) {
            games.add(g.split(" ")[0] + " " + g.split(" ")[1]);
        }
    }

    void addGame(String text, int possiblePlayers) {
        if(text.equalsIgnoreCase("")) text = "newGame";
        printWriter.println("CREATEGAME " + text.replaceAll("\\s+", "") + " " + possiblePlayers);
        printWriter.flush();
    }


    void disconnect() {
        printWriter.println("QUIT");
        printWriter.flush();
    }

    void joinGame(int id) {
        try {
            bufferedReader.ready();
            printWriter.println("JOIN " + id);
            line = bufferedReader.readLine();
            System.out.println(line);
            if (line.equals("YES")) {
                socket.close();
                socket = new Socket("localhost", 10000 + id);
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                printWriter = new PrintWriter(socket.getOutputStream(), true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getGames() {
        return games;
    }

    String getUserName() {
        return userName;
    }
}

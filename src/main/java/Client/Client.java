package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    //Connection and communication
    private static final int port = 8000;
    private static final String serverIP = "localhost";
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    public boolean isConnected = false;
    public boolean isInGame = false;
    public Gamer gamer;
    int currentClients = 1;

    Socket socket;
    //User stored variables
    private ArrayList<String> games = new ArrayList<>();
    private String userName;


    void setUserName(String userName) {
        this.userName = userName;
    }
    String getUserName() {
        return userName;
    }

    // ----------------------------------------------------------
    // Server communication
    // ----------------------------------------------------------

    public void connect() throws IOException {
        if (isConnected) return;
        socket = new Socket(serverIP, port);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printWriter = new PrintWriter(socket.getOutputStream(), true);
        isConnected = true;

        printWriter.println("USERNAME " + this.userName);
    }

    void disconnect() {
        printWriter.println("QUIT\n");
        printWriter.flush();
    }

    public void getGamesFromServer() {
        try {

            printWriter.println("GETGAMES");
            String line = bufferedReader.readLine();
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
            if(g.startsWith("CurrentPlayers")) {
                String[] currPlayers = g.split(" ");
                currentClients = Integer.parseInt(currPlayers[1]);
                return;
            }

            games.add(g.split(" ")[0] + " " + g.split(" ")[1] + " \t\t\t " + g.split(" ")[2] + " \t\t\t " + g.split(" ")[3] + "/" + g.split(" ")[4]);
        }
    }

    public ArrayList<String> getGames() {
        return games;
    }

    void addGame(String text, int possiblePlayers) {
        if(text.equalsIgnoreCase("")) text = "newGame";
        printWriter.println("CREATEGAME " + text.replaceAll("\\s+", "") + " " + possiblePlayers + " ");
        printWriter.flush();
    }

    public void joinGame(int id) {
        try {
            printWriter.println("JOIN " + id);
            String line = bufferedReader.readLine();
            if (line.startsWith("YES")) {
                this.gamer = new Gamer(serverIP, 50000 + id);

                new Thread(gamer).start();
                isInGame = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteGame(int gameID) {
        printWriter.println("DELETE " + gameID);
    }
}

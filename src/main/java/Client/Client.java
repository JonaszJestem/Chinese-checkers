package Client;

import Game.GameGUI;
import Map.Field;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

public class Client {
    private final int port = 8000;
    static ArrayList<String> games = new ArrayList<>();
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    public boolean isConnected = false;
    public Socket gameSocket;

    private String userName;
    Socket socket;
    GameGUI gameGUI;
    private String line;
    private PrintWriter gameWriter;
    private BufferedReader gameReader;

    private ObjectInputStream objectInputStream;

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
                gameSocket = new Socket("localhost", 10000 + id);
                gameReader = new BufferedReader(new InputStreamReader(gameSocket.getInputStream()));
                gameWriter = new PrintWriter(gameSocket.getOutputStream(), true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashSet<Field> getMap() {
        try {
            gameWriter.println("GETMAP");
            HashSet<Field> map = new HashSet<>();
            while (true) {
                line = gameReader.readLine();
                System.out.println(line);
                if (line.equalsIgnoreCase("END")) break;
                String[] parameters = line.split(" ");
                map.add(new Field(Double.parseDouble(parameters[0]), Double.parseDouble(parameters[1]), Double.parseDouble(parameters[2])));
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getGames() {
        return games;
    }

    String getUserName() {
        return userName;
    }
}

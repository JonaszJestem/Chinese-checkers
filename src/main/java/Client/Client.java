package Client;

import Game.GameGUI;
import Map.ColorEnum;
import Map.Field;
import Map.Map;
import Map.Star;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

import static java.lang.Math.*;

public class Client {
    //Connection and communication
    private static final int port = 8000;
    private static final String serverIP = "localhost";
    //Game connection and communication
    public Socket gameSocket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    public boolean isConnected = false;
    private String line;
    public boolean isInGame = false;
    private PrintWriter gameWriter;
    private BufferedReader gameReader;
    public Map map;
    Socket socket;
    GameGUI gameGUI;
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
    }

    void disconnect() {
        printWriter.println("QUIT");
        printWriter.flush();
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

    public ArrayList<String> getGames() {
        return games;
    }

    void addGame(String text, int possiblePlayers) {
        if(text.equalsIgnoreCase("")) text = "newGame";
        printWriter.println("CREATEGAME " + text.replaceAll("\\s+", "") + " " + possiblePlayers);
        printWriter.flush();
    }

    public void joinGame(int id) {
        try {
            bufferedReader.ready();
            printWriter.println("JOIN " + id);
            line = bufferedReader.readLine();
            if (line.equals("YES")) {
                gameSocket = new Socket(serverIP, 10000 + id);
                gameReader = new BufferedReader(new InputStreamReader(gameSocket.getInputStream()));
                gameWriter = new PrintWriter(gameSocket.getOutputStream(), true);
                map = new Star(600, 600);
                isInGame = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ----------------------------------------------------------
    // Game communication
    // ----------------------------------------------------------

    public void getMap() {
        try {
            gameWriter.println("GETMAP");
            HashSet<Field> fieldsFromServer = new HashSet<>();
            while (true) {
                line = gameReader.readLine();
                if (line.equalsIgnoreCase("END")) break;
                String[] parameters = line.split(" ");
                fieldsFromServer.add(new Field(Double.parseDouble(parameters[0]), Double.parseDouble(parameters[1]), Double.parseDouble(parameters[2])));
            }
            this.map.setFieldList(fieldsFromServer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMove() {
        gameWriter.println("SETMAP");
        StringBuilder stringBuilder = new StringBuilder();
        for (Field f : map.getFieldList()) {
            stringBuilder.append(f.x + " " + f.y + " " + f.getColor() + "\n");
        }
        gameWriter.write(stringBuilder.toString() + "END\n");
        gameWriter.flush();
    }

    public void move(Point moveFrom, Point moveTo) {
        Field from = null;
        Field to = null;

        for (Field f : map.getFieldList()) {
            if (f.contains(moveFrom) && f.getColor() != ColorEnum.WHITE) {
                from = f;
            }
            if (f.contains(moveTo) && f.getColor() == ColorEnum.WHITE) {
                to = f;
            }
        }

        if (from != null && to != null && distance(to, from) < 90) {
            makeMove(from, to);
        }
    }

    private void makeMove(Field from, Field to) {
        if (distance(to, from) <= 45) {
            to.setColor(from.getColor());
            from.setColor(ColorEnum.WHITE);
        } else if (distance(to, from) <= 90) {
            Point middle = getMiddle(from, to);
            for (Field f : map.getFieldList()) {
                if (f.contains(middle) && f.getColor() != ColorEnum.WHITE) {
                    to.setColor(from.getColor());
                    from.setColor(ColorEnum.WHITE);
                    break;
                }
            }
        }
        sendMove();
    }

    private Point getMiddle(Field f1, Field f2) {
        return new Point(abs(new Double((f1.x + f2.x + map.getFieldSize()) / 2).intValue()),
                abs(new Double((f1.y + f2.y + map.getFieldSize()) / 2).intValue()));
    }

    private double distance(Field field, Field f) {
        System.out.println("Distance: " + sqrt(pow(abs(field.x - f.x), 2) + pow(abs(field.y - f.y), 2)));
        return sqrt(pow(abs(field.x - f.x), 2) + pow(abs(field.y - f.y), 2));
    }
}

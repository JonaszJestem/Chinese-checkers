package Client;

import Map.ColorEnum;
import Map.Field;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Gamer implements Runnable {
    private final String serverIP;
    private final int port;
    private Socket gameSocket;
    public volatile ConcurrentHashMap<Field, ColorEnum> map;
    private PrintWriter gameWriter;
    private BufferedReader gameReader;
    private ColorEnum myColor;
    private GameGUI gameGUI;
    private Field from = null, to = null;

    Gamer(String serverIP, int port) {
        this.serverIP = serverIP;
        this.port = port;
        map = new ConcurrentHashMap<>();
    }

    @Override
    public void run() {
        try {
            gameSocket = new Socket(serverIP, port);
            gameReader = new BufferedReader(new InputStreamReader(gameSocket.getInputStream()));
            gameWriter = new PrintWriter(gameSocket.getOutputStream(), true);
            getMyColor();


        } catch (IOException e) {
            System.out.println("Could not connect to server");
        }

        gameGUI = new GameGUI(this);
        String line;

        while (true) {
            try {
                System.out.println("Waiting for server response");
                line = gameReader.readLine();
                System.out.println(line);
                if (line.equalsIgnoreCase("MAP")) {
                    getMap();
                }
                else if (line.equalsIgnoreCase("MOVE")) {
                    System.out.println("Able to move");
                    gameGUI.allowMoving();
                    while(true) {
                        gameReader.readLine();
                        if (line.equalsIgnoreCase("SUCCESFUL")) {
                            System.out.println("Move successful");
                            applyMove();
                            gameGUI.disableMoving();
                            break;
                        } else if (line.equalsIgnoreCase("WRONGMOVE")) {
                            System.out.println("Wrong move");
                        }
                    }
                }
            } catch (IOException ex) {
                return;
            }
        }
    }

    private void applyMove() {
        ColorEnum color = map.get(from);
        map.put(from, ColorEnum.WHITE);
        map.put(to, color);
    }

    private void getMyColor() {
        gameWriter.println("GETCOLOR");
        try {
            String line = gameReader.readLine();
            myColor = ColorEnum.valueOf(line);
            System.out.println("MyColor: " + myColor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getMap() {
        synchronized (map) {
            try {
                map.clear();
                String line;
                while (true) {
                    line = gameReader.readLine();
                    if (line.equalsIgnoreCase("END")) break;
                    String[] parameters = line.split(" ");
                    map.put(new Field(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1])), ColorEnum.valueOf(parameters[2]));
                }
                System.out.println("Got the map from server");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMove(Point p, Point q) {
        System.out.println("In send move");
        map.keySet().forEach((f) -> {
            if (f.contains(p)) from = f;
            if (f.contains(q)) to = f;
        });

        if (from != null && to != null) {
            System.out.println(from + " " + to);
            System.out.println(map.get(to) == ColorEnum.WHITE);
            System.out.println(map.get(from));
            System.out.println("Sending move");
            gameWriter.println("MOVE " + from.x_int + " " + from.y_int + " " + to.x_int + " " + to.y_int + " " + myColor);
        }
    }

    public boolean waitForMove() {
        System.out.println("Waiting for move");
        String line;
        while (true) {
            try {
                line = gameReader.readLine();
                if (ColorEnum.valueOf(line) == myColor) {
                    System.out.println("My turn!");
                    return true;
                }
            } catch (IOException e) {
                return false;
            }
        }
    }
}

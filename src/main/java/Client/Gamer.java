package Client;

import Map.ColorEnum;
import Map.Field;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Gamer implements Runnable {
    private final String serverIP;
    private final int port;
    private Socket gameSocket;
    public volatile ConcurrentHashMap<Field, ColorEnum> map;
    PrintWriter gameWriter;
    BufferedReader gameReader;
    ColorEnum myColor;
    private GameGUI gameGUI;
    Field from = null, to = null;
    String line;


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

        while (true) {
            try {
                line = gameReader.readLine();
                System.out.println(line);

                if (line.equalsIgnoreCase("SUCCESSFUL")) {
                    System.out.println("Move successful");
                    gameGUI.disableMoving();
                    applyMove();
                }
                else if (line.equalsIgnoreCase("MAP")) {
                    getMap();
                }
                else if (line.equalsIgnoreCase("MOVE")) {
                    System.out.println("Able to move");
                    gameGUI.allowMoving();
                }

                from = null; to = null;
            } catch (IOException ex) {
                return;
            }
        }
    }

    private void applyMove() {
        ColorEnum color = getFieldList().get(from);
        synchronized (map) {
            map.put(from, ColorEnum.WHITE);
            map.put(to, color);
        }
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

    public synchronized ConcurrentHashMap<Field, ColorEnum> getFieldList() {
        ConcurrentHashMap<Field, ColorEnum> copyMap = map;
        return copyMap;
    }
}

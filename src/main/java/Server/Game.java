package Server;

import Map.ColorEnum;
import Map.Field;
import Map.Map;
import Map.Star;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;

public class Game implements Runnable {
    static int gameCounter;

    //Server settings
    private final int port;
    private final int maxPlayers;
    private final String gameName;
    private final int gameID;
    private final Map map;
    boolean IS_RUNNING = false;
    private ServerSocket gameServerSocket;
    private Socket clientSocket;
    private volatile List<GameThread> gameThreads = new ArrayList<>();
    private int currentPlayers = 0;
    private int movingPlayer = 0;

    public Game(String gameName, int maxPlayers) {
        this.gameID = gameCounter++;
        this.gameName = gameName;
        this.maxPlayers = maxPlayers;
        this.port = 50000 + gameID;
        map = new Star();
        map.buildWithPlayers(maxPlayers);
    }

    @Override
    public void run() {
        if (IS_RUNNING) return;

        Collections.synchronizedList(gameThreads);
        try {
            gameServerSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IS_RUNNING = true;

        map.buildWithPlayers(maxPlayers);

        while (true) {
            System.out.println("Waiting for connection");
            try {
                clientSocket = gameServerSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            GameThread gameThread = new GameThread(clientSocket, this);
            gameThreads.add(gameThread);
            gameThread.run();

            currentPlayers++;
        }
    }

    synchronized void removeInactivePlayers() {
        gameThreads.forEach((thread) -> {
            if (thread.getThreadGroup() == null) gameThreads.remove(thread);
        });
    }

    String getGameName() {
        return gameName;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    int getGameID() {
        return gameID;
    }

    int getCurrentPlayers() {
        return currentPlayers;
    }

    int getMovingPlayer() {
        return movingPlayer;
    }

    Map getMap() {
        return this.map;
    }

    void setMap(HashMap<Field, ColorEnum> map) {
        System.out.println("Setting new fields");
        this.map.getFieldList().clear();
        this.map.setFieldList(map);
        movingPlayer = (movingPlayer + 1) % maxPlayers;
    }

    public boolean move(Field from, Field to) {
        ColorEnum color;
        if (distance(from, to) < 80) {
            if (distance(from, to) <= 45) {
                color = map.getFieldList().get(from);
                map.getFieldList().put(from, ColorEnum.WHITE);
                map.getFieldList().put(to, color);
                return true;
            } else if (distance(from, to) <= 80) {
                Point middle = getMiddle(from, to);
                for (java.util.Map.Entry<Field, ColorEnum> f : map.getFieldList().entrySet()) {
                    if (f.getKey().contains(middle) && f.getValue().getColor() != ColorEnum.WHITE) {
                        color = map.getFieldList().get(from);
                        map.getFieldList().put(from, ColorEnum.WHITE);
                        map.getFieldList().put(to, color);
                        return true;
                    }
                }
            }
        }
        return false;
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

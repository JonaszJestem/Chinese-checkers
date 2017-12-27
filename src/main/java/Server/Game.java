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
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Math.abs;
import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;
import static java.lang.Thread.sleep;

public class Game implements Runnable {
    static int gameCounter;

    //Server settings
    private final int port;
    private final int maxPlayers;
    private final String gameName;
    private final int gameID;
    public final Map map;
    boolean IS_RUNNING = false;
    private ServerSocket gameServerSocket;
    private Socket clientSocket;
    private volatile List<GameThread> gameThreads = new ArrayList<>();
    private int currentPlayers = 0;
    private int movingPlayer = 0;

    Game(String gameName, int maxPlayers) {
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

        gameThreads = Collections.synchronizedList(gameThreads);
        try {
            gameServerSocket = new ServerSocket(port);
            System.out.println("Created game with id: " + gameID);
            System.out.println("Running on port: " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IS_RUNNING = true;

        map.buildWithPlayers(maxPlayers);

        while (true) {
            removeInactivePlayers();
            if (maxPlayers == currentPlayers) startGame();
            System.out.println("Waiting for connection");
            try {
                clientSocket = gameServerSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            GameThread gameThread = new GameThread(clientSocket, this, gameThreads.size());
            gameThreads.add(gameThread);
            gameThread.start();

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

    synchronized ConcurrentHashMap<Field, ColorEnum> getMap() {
        return this.map.getFieldList();
    }

    synchronized ColorEnum getColor() {
        return this.map.getColor();
    }

    private void startGame() {
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Starting game & notifying " + movingPlayer);
        synchronized (map) {
            map.notifyAll();
        }
    }

    public synchronized boolean move(Field from, Field to, ColorEnum playersColor) {
        synchronized (map) {
            ColorEnum color = getMap().get(from);
            System.out.println("Players: " + playersColor + " tile: " + color);
            if (playersColor != color) {
                System.out.println("Moving with wrong color");
                return false;
            }
            System.out.println("Got move from player:" + from + " " + to + " " + playersColor);
            System.out.println("Distance: " + distance(from, to));
            if (distance(from, to) < 80) {
                if (distance(from, to) <= 45) {
                    System.out.println("single move");
                    map.getFieldList().put(from, ColorEnum.WHITE);
                    map.getFieldList().put(to, color);
                    movingPlayer = (movingPlayer + 1) % maxPlayers;
                    System.out.println("Notifying " + movingPlayer);
                    map.notifyAll();
                    return true;
                } else if (distance(from, to) <= 80) {
                    System.out.println("doublemove");
                    Point middle = getMiddle(from, to);
                    for (java.util.Map.Entry<Field, ColorEnum> f : getMap().entrySet()) {
                        if (f.getKey().contains(middle) && f.getValue().getColor() != ColorEnum.WHITE) {
                            map.getFieldList().put(from, ColorEnum.WHITE);
                            map.getFieldList().put(to, color);
                            movingPlayer = (movingPlayer + 1) % maxPlayers;
                            System.out.println("Notifying " + movingPlayer);
                            map.notifyAll();
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    private Point getMiddle(Field f1, Field f2) {
        Double x = (f1.x + f2.x + map.getFieldSize()) / 2.0;
        Double y = (f1.y + f2.y + map.getFieldSize()) / 2.0;
        return new Point(x.intValue(), y.intValue());
    }

    private double distance(Field field, Field f) {
        return sqrt(pow(abs(field.x - f.x), 2) +
                        pow(abs(field.y - f.y), 2)
                    );
    }
}
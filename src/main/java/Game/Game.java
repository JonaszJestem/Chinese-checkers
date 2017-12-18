package Game;

import Map.Field;
import Map.Map;
import Map.Star;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Game implements Runnable {
    public static int gameCounter;

    //Server settings
    private int port;
    private ServerSocket serverSocket;
    private Socket socket;


    private List<GameThread> gameThreads = new ArrayList<>();
    private int id;
    private String name;
    private int maxPlayers;
    private int currentPlayers = 0;
    boolean isRunning = false;
    private Map map;



    public Game(String name, int maxPlayers) {
        this.id = gameCounter++;
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.port = 10000 + id;
        map = new Star(500, 500);
        map.buildWithPlayers(maxPlayers);
    }

    @Override
    public void run() {
        Collections.synchronizedList(gameThreads);

        if (isRunning) return;
        try {
            Game.gameCounter++;
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        isRunning = true;
        while (true) {
            System.out.println("Waiting for connection");
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            gameThreads.add(new GameThread(socket, this));
            gameThreads.get(gameThreads.size() - 1).start();

            currentPlayers++;
        }
    }

    private synchronized void removeInactivePlayers() {
        for (GameThread gt : gameThreads) {
            if (gt.getThreadGroup() == null) {
                gameThreads.remove(gt);
            }
        }
    }

    public String getGameName() {
        return name;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getGameID() {
        return id;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public Map getMap() {
        return this.map;
    }

    public void setMap(HashSet<Field> map) {
        System.out.println("Setting new fields");
        this.map.setFieldList(map);
    }
}

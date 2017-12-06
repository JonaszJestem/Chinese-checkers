package Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Game implements Runnable {
    public static int gameCounter;
    private int port;
    private ServerSocket serverSocket;
    private Socket socket;
    private ArrayList<GameThread> gameThreads = new ArrayList<>();
    private int id;
    private String name;
    private int maxPlayers;
    private int currentPlayers = 0;
    //private GameMap gameMap;

    public Game(String name, int maxPlayers) {
        this.id = gameCounter++;
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.port = 10000 + id;
        //gameMap = new DefaultMap();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            Game.gameCounter = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            System.out.println("Waiting for connection");
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            gameThreads.add(new GameThread(socket, this));
            gameThreads.get(gameThreads.size() - 1).start();
            removeInactivePlayers();
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
}

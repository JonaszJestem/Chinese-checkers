package Server;

import Game.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {

    private final int PORT = 8000;
    private ServerSocket serverSocket;
    private Socket socket;
    private boolean isReady = false;

    private ArrayList<ServerThread> serverThreads = new ArrayList<>();
    private List<Game> games = new ArrayList<>();

    public static void main(String[] args) {
        new Thread(new Server()).start();
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
            Game.gameCounter = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        isReady = true;

        while (true) {
            System.out.println("Waiting for connection");
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            serverThreads.add(new ServerThread(socket, this));
            serverThreads.get(serverThreads.size() - 1).start();
            removeInactivePlayers();
        }
    }

    private synchronized void removeInactivePlayers() {
        for (ServerThread st : serverThreads) {
            if (st.getThreadGroup() == null) {
                serverThreads.remove(st);
            }
        }
    }

    public int getNumOfPlayers() {
        removeInactivePlayers();
        return serverThreads.size();
    }

    public List<Game> getGames() {
        return games;
    }

    public Game getGame(int id) {
        for (Game g : games) {
            if (g.getGameID() == id) return g;
        }
        return null;
    }

    public void addGame(String name, int maxPlayers) {
        games.add(new Game(name, maxPlayers));
    }

    public boolean isReady() {
        return isReady;
    }
}

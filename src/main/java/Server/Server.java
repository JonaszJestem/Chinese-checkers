package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private boolean IS_RUNNING = false;

    private final List<ServerThread> serverThreads = new ArrayList<>();
    private final List<Game> gamesList = new ArrayList<>();

    public static void main(String[] args) {
        new Thread(new Server()).start();
    }

    public void run() {
        try {
            int PORT = 8000;
            serverSocket = new ServerSocket(PORT);
            Game.gameCounter = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        IS_RUNNING = true;

        while (true) {
            removeInactivePlayers();
            System.out.println("Waiting for client connection");
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            synchronized (serverThreads) {
                ServerThread serverThread = new ServerThread(clientSocket, this);
                serverThreads.add(serverThread);
                serverThread.start();
            }
        }
    }

    synchronized void removeInactivePlayers() {
        synchronized (serverThreads) {
            serverThreads.removeIf(st -> st.getThreadGroup() == null);
        }
    }

    public int getNumOfPlayers() {
        removeInactivePlayers();
        return serverThreads.size();
    }

    public synchronized List<Game> getGames() {
        return gamesList;
    }

    public Game getGame(int id) {
        synchronized (gamesList) {
            return gamesList.stream().filter(g -> g.getGameID() == id).findFirst().orElse(null);
        }
    }

    public void runGame(int id) {
        synchronized (gamesList) {
            Game game = gamesList.stream().filter(g -> g.getGameID() == id).findFirst().orElse(null);
            new Thread(game).start();
        }
    }

    public void addGame(String name, int maxPlayers, String gameMaster) {
        synchronized (gamesList) {
            gamesList.add(new Game(name, maxPlayers, gameMaster));
        }
    }
    public void addGame(String name, int maxPlayers) {
        synchronized (gamesList) {
            gamesList.add(new Game(name, maxPlayers));
        }
    }

    public boolean isRunning() {
        return IS_RUNNING;
    }

    public void deleteGame(Game g) {
        synchronized (gamesList) {
            gamesList.remove(g);
        }
    }
}

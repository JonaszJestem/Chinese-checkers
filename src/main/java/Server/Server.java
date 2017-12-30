package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Server implements Runnable {
    private ServerSocket serverSocket;
    private final int PORT = 8000;
    private Socket clientSocket;
    private boolean IS_RUNNING = false;

    private volatile List<ServerThread> serverThreads = new ArrayList<>();
    private volatile List<Game> gamesList = new ArrayList<>();

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

    private synchronized void removeInactivePlayers() {
        synchronized (serverThreads) {
            for (Iterator<ServerThread> iterator = serverThreads.iterator(); iterator.hasNext();) {
                ServerThread st = iterator.next();
                if (st.getThreadGroup() == null) {
                    iterator.remove();
                }
            }
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

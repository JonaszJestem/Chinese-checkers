import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class Server implements Runnable {

    private final int PORT = 8000;
    private ServerSocket serverSocket;
    private Socket socket;
    private boolean isReady = false;

    private List<Game> games = new ArrayList<>();

    public static void main(String[] args) {
        Server s = new Server();
        Thread st = new Thread(s);
        st.start();
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Created server");
        isReady = true;

        while (true) {
            System.out.println("Waiting for client connection");
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            new ServerThread(socket, this).start();
        }
    }

    public synchronized List<Game> getGames() {
        return games;
    }

    //TODO: IMPLEMENT MAKING NEW GAME
    public void addGame(String name, int numOfPlayers) {
        games.add(new Game(name, numOfPlayers));
    }

    public boolean isReady() {
        return isReady;
    }
}

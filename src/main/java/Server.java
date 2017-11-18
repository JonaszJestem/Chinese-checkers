import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private int PORT;
    private ServerSocket serverSocket;
    private Socket socket;
    private int usersCounter = 0;

    Server(int port) {
        this.PORT = port;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();

        }
        System.out.println("Created server");

        while (true) {
            System.out.println("Waiting for client connection");
            try {
                socket = serverSocket.accept();
                usersCounter++;
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            // new thread for a client
            new ServerThread(socket).start();
        }
    }

    int getConnectedUsers() {
        return usersCounter;
    }
}

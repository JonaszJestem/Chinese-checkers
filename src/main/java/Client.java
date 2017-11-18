import java.io.*;
import java.net.Socket;

class Client {
    private static int id = 0;
    private final int port = 8000;
    private Socket socket;
    private OutputStream outputStream;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private String userName;

    String getUserName() {
        return userName;
    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    void connect() throws IOException {
        socket = new Socket("localhost", port);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printWriter = new PrintWriter(socket.getOutputStream(), true);

        printWriter.println("I'm connected! ~" + id);
        id++;

        while (true) {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
            String input = inputReader.readLine();

            System.out.println(input);

            if (input.equalsIgnoreCase("EXIT")) {
                socket.close();
                break;
            }
        }
    }
}

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket socket;

    ServerThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
        System.out.println("Found client");
        InputStream inp;
        BufferedReader reader;
        DataOutputStream out;
        try {
            inp = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inp));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }
        String line;
        while (true) {
            try {
                line = reader.readLine();
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    socket.close();
                    return;
                } else {
                    out.writeBytes(line + "\n\r");
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
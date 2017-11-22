import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.Socket;

class ServerThread extends Thread {
    private Socket socket;
    private Server server;
    private String games;

    ServerThread(Socket clientSocket, Server server) {
        this.socket = clientSocket;
        this.server = server;
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
                } else if ((line.equalsIgnoreCase("GETGAMES"))) {
                    games = new Gson().toJson(server.getGames());
                    System.out.println("Sending: " + games);

                    out.writeBytes(games + "\n\r");
                    out.flush();
                } else if ((line.equalsIgnoreCase("CREATEGAME"))) {
                    line = reader.readLine();
                    JsonObject gameProp = new Gson().fromJson(line, new TypeToken<Game>() {
                    }.getType());
                    server.addGame(gameProp);
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
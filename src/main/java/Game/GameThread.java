package Game;

import Map.Field;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;

public class GameThread extends Thread {
    private Socket socket;
    private Game server;
    private String games;
    private InputStream inputStream;
    private BufferedReader reader;
    private DataOutputStream outputStream;

    private ObjectOutputStream objectOutputStream;


    GameThread(Socket clientSocket, Game server) {
        this.socket = clientSocket;
        this.server = server;
    }

    public void run() {
        System.out.println("Found gamer");

        try {
            inputStream = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String line;
        while (true) {
            try {
                line = reader.readLine();
                if ((line.equalsIgnoreCase("GETMAP"))) {
                    HashSet<Field> map = server.getMap();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (Field f : map) {
                        stringBuilder.append(f.x + " " + f.y + " " + f.width + "\n");
                    }
                    System.out.println("Sending: " + stringBuilder.toString() + "END\n");
                    outputStream.writeBytes(stringBuilder.toString() + "END\n");
                    outputStream.flush();
                } else if ((line.equalsIgnoreCase("SETMAP"))) {
                    HashSet<Field> map = new HashSet<>();
                    while (true) {
                        line = reader.readLine();
                        if (line.equalsIgnoreCase("END")) break;
                        System.out.println(line);
                        String[] parameters = line.split(" ");
                        map.add(new Field(Double.parseDouble(parameters[0]), Double.parseDouble(parameters[1]), Double.parseDouble(parameters[2])));
                    }
                    System.out.println("Map set to: " + map);
                    server.setMap(map);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}

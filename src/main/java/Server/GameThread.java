package Server;

import Map.ColorEnum;
import Map.Field;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class GameThread extends Thread {
    private Socket clientSocket;
    private Game server;
    private InputStream inputStream;
    private BufferedReader reader;
    private DataOutputStream outputStream;

    private HashMap<Field, ColorEnum> map = new HashMap<>();

    GameThread(Socket clientSocket, Game server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    public void run() {
        System.out.println("Found gamer");

        try {
            inputStream = clientSocket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String line;
        while (true) {
            try {
                line = reader.readLine();
                if ((line.equalsIgnoreCase("GETMAP"))) {
                    sendMapToClient();
                } else if ((line.startsWith("MOVE"))) {
                    sendMoveToServer(line);
                } else if ((line.equalsIgnoreCase("GETCOLOR"))) {
                    outputStream.writeBytes(server.getMap().getColor().toString());
                }
                outputStream.writeBytes("END\n");
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void sendMapToClient() {
        map = server.getMap().getFieldList();
        map.forEach((k, v) -> {
            try {
                outputStream.writeBytes(k.x_int + " " + k.y_int + " " + v.getColor() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void sendMoveToServer(String line) {
        try {
            String[] move = line.split(" ");
            Field f1 = new Field(Integer.parseInt(move[1]), Integer.parseInt(move[2]));
            Field f2 = new Field(Integer.parseInt(move[3]), Integer.parseInt(move[4]));
            if (server.move(f1, f2)) {
                outputStream.writeBytes("SUCCESFUL\n");
            } else {
                outputStream.writeBytes("WRONGMOVE");
            }
        } catch (IOException ex) {
            return;
        }
    }
}

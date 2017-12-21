package Server;

import Map.ColorEnum;
import Map.Field;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class GameThread extends Thread {
    private Socket clientSocket;
    private Game server;
    private InputStream inputStream;
    private BufferedReader reader;
    private DataOutputStream outputStream;
    String line;
    private ColorEnum clientColor;
    private ConcurrentHashMap<Field, ColorEnum> map = new ConcurrentHashMap<>();

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
            System.out.println("Disconnected");
            this.interrupt();
            return;
        }

        waitForCommands();
    }

    private void waitForCommands() {
        while (true) {
            try {
                System.out.println("Waiting for info from gamer");
                line = reader.readLine();
                if ((line.equalsIgnoreCase("GETMAP"))) {
                    sendMapToClient();
                    outputStream.writeBytes("END\n");
                } else if ((line.startsWith("MOVE"))) {
                    System.out.println("Got move from client");
                    System.out.println(line);
                    sendMoveToServer(line);
                } else if ((line.equalsIgnoreCase("GETCOLOR"))) {
                    clientColor = server.getColor();
                    outputStream.writeBytes(clientColor.toString() + "\n");
                }
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void sendMapToClient() {
        map = server.getMap();
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
            Field from = new Field(Integer.parseInt(move[1]), Integer.parseInt(move[2]));
            Field to = new Field(Integer.parseInt(move[3]), Integer.parseInt(move[4]));
            if (server.move(from, to, ColorEnum.valueOf(move[5]))) {
                outputStream.writeBytes("SUCCESFUL\n");
            } else {
                outputStream.writeBytes("WRONGMOVE\n");
            }
        } catch (IOException ex) {
            return;
        }
    }

    void notifyAboutMove() {
        try {
            outputStream.writeBytes(clientColor.toString() + "\n");
            System.out.println("Notified");
            waitForCommands();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

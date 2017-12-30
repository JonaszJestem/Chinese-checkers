package Server;

import Map.ColorEnum;
import Map.Field;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class GameThread extends Thread {
    private Socket clientSocket;
    private Game server;
    private InputStream inputStream;
    private BufferedReader reader;
    private DataOutputStream outputStream;
    private String line;
    private ColorEnum clientColor;
    private ConcurrentHashMap<Field, ColorEnum> map = new ConcurrentHashMap<>();
    private final int id;


    GameThread(Socket clientSocket, Game server, int id) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.id = id;
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

        try {
            line = reader.readLine();
            if ((line.equalsIgnoreCase("GETCOLOR"))) {
                clientColor = server.getColor();
                outputStream.writeBytes(clientColor.toString() + "\n");
            }

            map = server.getMap();

            outputStream.writeBytes("MAP\n");
            sendMapToClient();
            outputStream.writeBytes("END\n");

        } catch (IOException ex) {
            System.out.println("Couldn't set up gamer");
            this.interrupt();
            return;
        }



        while (true) {
            try {

                synchronized (server.map) {

                    map = server.getMap();

                    outputStream.writeBytes("MAP\n");
                    sendMapToClient();
                    outputStream.writeBytes("END\n");

                    System.out.println("Thread " + id + ": Before wait");
                    server.map.wait();
                    System.out.println("Thread " + id + ": After wait");

                }

                System.out.println("Thread " + id + ": " + server.getMovingPlayer() + " " +id);

                if (server.getMovingPlayer() == id) {
                    System.out.println("Thread " + id + ": Tell player about move");
                    outputStream.writeBytes("MOVE\n");
                    outputStream.flush();
                    while (server.getMovingPlayer() == id) {
                        line = reader.readLine();
                        if ((line.startsWith("MOVE"))) {
                            System.out.println("Thread " + id + ": Got move from client");
                            System.out.println(line);
                            sendMoveToServer(line);
                        }
                    }
                }
                outputStream.flush();
            }
            catch (Exception e) {
                System.out.println("Connection lost");
                break;
            }
        }
    }

    private void sendMapToClient() {
        System.out.println("Thread " + id + ": Sending map to client");
        map.forEach((k, v) -> {
            try {
                outputStream.writeBytes(k.x_int + " " + k.y_int + " " + v.getColor() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void sendMoveToServer(String line) {
        System.out.println("Thread " + id + ": sending move to server");
        try {
            String[] move = line.split(" ");
            Field from = new Field(Integer.parseInt(move[1]), Integer.parseInt(move[2]));
            Field to = new Field(Integer.parseInt(move[3]), Integer.parseInt(move[4]));
            if (server.move(from, to, ColorEnum.valueOf(move[5]))) {
                System.out.println("Thread " + id + ": succesful");
                outputStream.writeBytes("SUCCESSFUL\n");

                map = server.getMap();

                outputStream.writeBytes("MAP\n");
                sendMapToClient();
                outputStream.writeBytes("END\n");

            } else {
                System.out.println("Thread " + id + ":Wrong move");
                outputStream.writeBytes("MOVE\n");
            }
            outputStream.flush();
        } catch (IOException ex) {
            System.out.println("Can't send move to server");
        }
    }
}

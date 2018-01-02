package Server;

import Map.ColorEnum;
import Map.Field;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

class GameThread extends Thread {
    private final Socket clientSocket;
    private final Game server;
    private DataOutputStream outputStream;
    ColorEnum clientColor;
    private ConcurrentHashMap<Field, ColorEnum> map = new ConcurrentHashMap<>();
    private final int id;


    GameThread(Socket clientSocket, Game server, int id) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.id = id;
    }

    public void run() {
        System.out.println("Found gamer");

        BufferedReader reader;
        try {
            InputStream inputStream = clientSocket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            cleanUp();
            return;
        }

        String line;
        try {
            line = reader.readLine();
            if ((line.equalsIgnoreCase("GETCOLOR"))) {
                clientColor = server.getColor();
                outputStream.writeBytes(clientColor.toString() + "\n");
            }

        } catch (IOException ex) {
            cleanUp();
        }
/*
        while(true) {
            try {
                line = reader.readLine();

                if(line.equalsIgnoreCase("ADDBOT")) server.addBot();
                else if(line.equalsIgnoreCase("READY")) break;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
*/
        while (true) {
            try {
                synchronized (server.map) {
                    map = server.getMap();

                    outputStream.writeBytes("MAP\n");
                    sendMapToClient();
                    outputStream.writeBytes(server.getMovingColor().toString() + "\n");

                    server.map.wait();

                    if(server.matchEnded) {
                        endMatch();
                    }
                }
                if(!clientSocket.isConnected()) cleanUp();

                if (server.getMovingPlayer() == id) {
                    System.out.println("Thread " + id + ": Tell player about move");
                    outputStream.writeBytes("MOVE\n");
                    sendMapToClient();
                    outputStream.writeBytes(server.getMovingColor().toString() + "\n");
                    outputStream.flush();
                    while (server.getMovingPlayer() == id) {
                        line = reader.readLine();
                        if ((line.startsWith("MOVE"))) {
                            System.out.println("Thread " + id + ": Got move from client");
                            System.out.println(line);
                            sendMoveToServer(line);
                        }
                        if ((line.startsWith("QUIT"))) {
                            System.out.println("Connection lost");
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                outputStream.flush();

            }
            catch (Exception e) {
                cleanUp();
                break;
            }
        }
    }

    private void endMatch() {
        try {
            outputStream.writeBytes("WIN " + server.getMovingColor());
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            cleanUp();
        }
    }

    private void sendMapToClient() {
        map.forEach((k, v) -> {
            try {
                outputStream.writeBytes(k.x_int + " " + k.y_int + " " + v.getColor() + "\n");
            } catch (IOException e) {
                cleanUp();
            }
        });
    }

    private void sendMoveToServer(String line) {
        System.out.println("Thread " + id + ": sending Move to server");
        try {
            String[] move = line.split(" ");
            Field from = new Field(Integer.parseInt(move[1]), Integer.parseInt(move[2]));
            Field to = new Field(Integer.parseInt(move[3]), Integer.parseInt(move[4]));

            String serverResponse = server.checkMove(from, to, ColorEnum.valueOf(move[5]));
            if (serverResponse.equalsIgnoreCase("OK")) {
                System.out.println("Thread " + id + ": succesful");
                outputStream.writeBytes("SUCCESSFUL\n");

                map = server.getMap();

                outputStream.writeBytes("MAP\n");
                sendMapToClient();
                outputStream.writeBytes("END\n");

            } else if (serverResponse.equalsIgnoreCase("WRONG")) {
                System.out.println("Thread " + id + ":Wrong Move");
                outputStream.writeBytes("MOVE\n");
                sendMapToClient();
                outputStream.writeBytes(server.getMovingColor().toString() + "\n");
            } else if (serverResponse.startsWith("WIN")) {
                System.out.println(serverResponse);
                outputStream.writeBytes(serverResponse + "\n");
            }
            outputStream.flush();
        } catch (IOException ex) {
            cleanUp();
        }
    }

    private void cleanUp() {
        System.out.println("Connection lost");
        Thread.currentThread().interrupt();
        server.removeInactivePlayers();
    }
}

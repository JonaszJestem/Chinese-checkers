package Server;

import Game.Game;

import java.io.*;
import java.net.Socket;

class ServerThread extends Thread {
    private Socket socket;
    private Server server;
    private String games;
    private InputStream inputStream;
    private BufferedReader reader;
    private DataOutputStream outputStream;

    ServerThread(Socket clientSocket, Server server) {
        this.socket = clientSocket;
        this.server = server;
    }

    public void run() {
        System.out.println("Found client");


        try {
            inputStream = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }

        while (true) {
            try {
                String line = reader.readLine();
                if ((line.equalsIgnoreCase("GETGAMES"))) {
                    games = buildGamesList();
                    outputStream.writeBytes(games + "\n");
                } else if ((line.startsWith("CREATEGAME"))) {
                    String[] game = line.split(" ");
                    server.addGame(game[1], Integer.parseInt(game[2]));
                } else if ((line.startsWith("JOIN"))) {
                    String[] game = line.split(" ");
                    Game g = server.getGame(Integer.parseInt(game[1]));
                    if (g.getMaxPlayers() > g.getCurrentPlayers()) {
                        if (g.getCurrentPlayers() == 0) {
                            server.runGame(Integer.parseInt(game[1]));
                        }
                        outputStream.writeBytes("YES\n");
                    } else outputStream.writeBytes("NO\n");
                } else if ((line.equalsIgnoreCase("QUIT"))) {
                    Thread.currentThread().interrupt();
                    break;
                }
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private String buildGamesList() {
        StringBuilder sb = new StringBuilder();
        for (Game g : server.getGames()) {
            sb.append(g.getGameID() + " ");
            sb.append(g.getGameName() + " ");
            sb.append(g.getMaxPlayers() + ";");
        }
        return sb.toString();
    }
}
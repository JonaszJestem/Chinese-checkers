package Server;

import java.io.*;
import java.net.Socket;

class ServerThread extends Thread {
    private Socket clientSocket;
    private Server server;

    private InputStream clientInputStream;
    private DataOutputStream clientOutputStream;
    private BufferedReader clientReader;

    ServerThread(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    public void run() {
        System.out.println("Found client");
        try {
            clientInputStream = clientSocket.getInputStream();
            clientReader = new BufferedReader(new InputStreamReader(clientInputStream));
            clientOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                String line = clientReader.readLine();
                if ((line.equalsIgnoreCase("GETGAMES"))) {
                    sendGamesToClient();
                } else if ((line.startsWith("CREATEGAME"))) {
                    createGame(line);
                } else if ((line.startsWith("JOIN"))) {
                    joinGame(line);
                } else if ((line.equalsIgnoreCase("QUIT"))) {
                    Thread.currentThread().interrupt();
                    break;
                }
                clientOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private void joinGame(String line) {
        String[] game = line.split(" ");
        Game g = server.getGame(Integer.parseInt(game[1]));
        //TODO: Concurrent modification
        //g.removeInactivePlayers();
        try {
            if (g.getMaxPlayers() > g.getCurrentPlayers()) {
                if (!g.IS_RUNNING) server.runGame(g.getGameID());
                clientOutputStream.writeBytes("YES\n");
            } else clientOutputStream.writeBytes("NO\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createGame(String line) {
        String[] game = line.split(" ");
        server.addGame(game[1], Integer.parseInt(game[2]));
    }

    private void sendGamesToClient() {
        String gamesList = buildGamesList();
        try {
            clientOutputStream.writeBytes(gamesList + "\n");
        } catch (IOException e) {
            e.printStackTrace();
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
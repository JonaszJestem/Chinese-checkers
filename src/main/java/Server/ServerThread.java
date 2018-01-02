package Server;

import java.io.*;
import java.net.Socket;

class ServerThread extends Thread {
    private final Socket clientSocket;
    private final Server server;

    private DataOutputStream clientOutputStream;
    private BufferedReader clientReader;
    private String userName;

    ServerThread(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    public void run() {
        System.out.println("Found client");
        try {
            InputStream clientInputStream = clientSocket.getInputStream();
            clientReader = new BufferedReader(new InputStreamReader(clientInputStream));
            clientOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            cleanUp();
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
                } else if ((line.startsWith("USERNAME"))) {
                    setUserName(line);
                } else if ((line.equalsIgnoreCase("QUIT"))) {
                    cleanUp();
                    break;
                } else if ((line.startsWith("DELETE"))) {
                    deleteGame(line);
                }
                clientOutputStream.flush();
            } catch (IOException e) {
                cleanUp();
                return;
            }
        }
    }

    private void deleteGame(String line) {
        String[] game = line.split(" ");
        Game g = server.getGame(Integer.parseInt(game[1]));
        if (g.gameMaster.equalsIgnoreCase(this.userName)) server.deleteGame(g);
    }

    private void setUserName(String line) {
        this.userName = line.split(" ")[1];
    }

    private void joinGame(String line) {
        String[] game = line.split(" ");
        Game g = server.getGame(Integer.parseInt(game[1]));
        g.removeInactivePlayers();
        try {
            if (g.getMaxPlayers() > g.getCurrentPlayers()) {
                if (!g.IS_RUNNING) server.runGame(g.getGameID());
                clientOutputStream.writeBytes("YES\n");
            } else clientOutputStream.writeBytes("NO\n");
        } catch (IOException e) {
            cleanUp();
        }
    }

    private void createGame(String line) {
        String[] game = line.split(" ");
        server.addGame(game[1], Integer.parseInt(game[2]), userName);
    }

    private void sendGamesToClient() {
        String gamesList = buildGamesList();
        try {
            clientOutputStream.writeBytes(gamesList + "\n");
        } catch (IOException e) {
            cleanUp();
        }
    }

    private String buildGamesList() {
        StringBuilder sb = new StringBuilder();
        for (Game g : server.getGames()) {
            sb.append(g.getGameID()).append(" ");
            sb.append(g.getGameName()).append(" ");
            sb.append(g.getGameMaster()).append(" ");
            sb.append(g.getCurrentPlayers()).append(" ");
            sb.append(g.getMaxPlayers()).append(";");
        }
        sb.append("CurrentPlayers ").append(server.getNumOfPlayers());
        return sb.toString();
    }

    private void cleanUp() {
        System.out.println("Connection lost");
        Thread.currentThread().interrupt();
        server.removeInactivePlayers();
    }
}
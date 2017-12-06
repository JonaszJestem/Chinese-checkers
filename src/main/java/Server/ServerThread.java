package Server;

import Game.Game;

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
                if ((line.equalsIgnoreCase("GETGAMES"))) {
                    games = buildGamesList();
                    System.out.println(games);
                    out.writeBytes(games + "\n");
                    out.flush();
                } else if ((line.startsWith("CREATEGAME"))) {
                    String[] game = line.split(" ");
                    server.addGame(game[1], Integer.parseInt(game[2]));
                } else if ((line.startsWith("JOIN"))) {
                    String[] game = line.split(" ");
                    Game g = server.getGame(Integer.parseInt(game[1]));
                    System.out.println("Trying to join game " + game[1]);
                    if (g.getMaxPlayers() > g.getCurrentPlayers()) {
                        if (g.getCurrentPlayers() == 0) g.run();
                        out.writeBytes("YES\n");
                    } else out.writeBytes("NO\n");
                    out.flush();
                } else if ((line.equalsIgnoreCase("QUIT"))) {
                    Thread.currentThread().interrupt();
                    break;
                }
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
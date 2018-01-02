package Server;

import Map.ColorEnum;
import Map.Field;
import Map.Map;
import Map.Star;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Math.abs;
import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;
import static java.lang.Thread.sleep;

public class Game implements Runnable {
    static int gameCounter;

    //Server settings
    private final int port;
    private final int maxPlayers;
    private final String gameName;
    private final int gameID;
    public final Map map;
    boolean IS_RUNNING = false;
    private ServerSocket gameServerSocket;
    private Socket clientSocket;
    private final List<GameThread> gameThreads = new ArrayList<>();
    private int currentPlayers = 0;
    private int movingPlayer = 0;
    boolean matchEnded = false;
    private HashMap<ColorEnum, Integer> atEndPoints = new HashMap<>();
    String gameMaster;

    Game(String gameName, int maxPlayers) {
        this.gameID = gameCounter++;
        this.gameName = gameName;
        this.maxPlayers = maxPlayers;
        this.port = 50000 + gameID;
        map = new Star();
        map.buildWithPlayers(maxPlayers);

        for(ColorEnum c: map.availableColors) {
            atEndPoints.put(c,0);
        }
    }

    Game(String gameName, int maxPlayers, String gameMaster) {
        this(gameName,maxPlayers);
        this.gameMaster = gameMaster;
    }

    @Override
    public void run() {
        if (IS_RUNNING) return;

        try {
            gameServerSocket = new ServerSocket(port);
            System.out.println("Created game with id: " + gameID);
            System.out.println("Running on port: " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IS_RUNNING = true;


        while (true) {
            removeInactivePlayers();
            if (maxPlayers == currentPlayers) startGame();
            System.out.println("Waiting for connection");
            try {
                clientSocket = gameServerSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            synchronized (gameThreads) {
                GameThread gameThread = new GameThread(clientSocket, this, gameThreads.size());
                gameThreads.add(gameThread);
                gameThread.start();
                currentPlayers++;
            }
        }
    }

    synchronized void removeInactivePlayers() {
        synchronized (gameThreads) {
            gameThreads.removeIf(gt -> gt.isInterrupted());
        }
    }

    String getGameName() {
        return gameName;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    int getGameID() {
        return gameID;
    }

    int getCurrentPlayers() {
        removeInactivePlayers();
        return currentPlayers;
    }

    int getMovingPlayer() {
        return movingPlayer;
    }

    ColorEnum getMovingColor() {
        return gameThreads.get(movingPlayer).clientColor;
    }

    synchronized ConcurrentHashMap<Field, ColorEnum> getMap() {
        return this.map.getFieldList();
    }

    synchronized ColorEnum getColor() {
        return this.map.getColor();
    }

    private void startGame() {
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Starting game & notifying " + movingPlayer);
        synchronized (map) {
            map.notifyAll();
        }
    }

    public synchronized String checkMove(Field from, Field to, ColorEnum playersColor) {
        System.out.println(atEndPoints);
        synchronized (map) {

            Field fromMap = null, toMap = null;
            for(Field f: map.getFieldList().keySet()) {
                if (f.equals(from)) {
                    fromMap = f;
                }
                if (f.equals(to)) {
                    toMap = f;
                }
            }

            ColorEnum color = getMap().get(from);
            if (playersColor != color) {
                System.out.println("Moving with wrong color");
                return "WRONG";
            }
            if(getMap().get(to) != ColorEnum.WHITE) {
                System.out.println("Field taken");
                return "WRONG";
            }

            //If checker wants to go out from endPoints area, return false
            if(fromMap.isEndPoint(playersColor) && !toMap.isEndPoint(playersColor)) {
                System.out.println("Going out of endPoints area");
                return "WRONG";
            }

            if (distance(from, to) < 80) {
                if (distance(from, to) <= 45) {
                    String moveStatus = moveAndNotify(fromMap, toMap, playersColor);
                    if(moveStatus.equals("WIN")) return "WIN " + playersColor;
                    return "OK";
                } else if (distance(from, to) <= 80) {
                    Point middle = getMiddle(from, to);
                    for (java.util.Map.Entry<Field, ColorEnum> f : getMap().entrySet()) {
                        if (f.getKey().contains(middle) && f.getValue().getColor() != ColorEnum.WHITE) {
                            String moveStatus = moveAndNotify(fromMap, toMap, playersColor);
                            if(moveStatus.equals("WIN")) return "WIN " + playersColor;
                            return "OK";
                        }
                    }
                }
            }
            return "OK";
        }
    }

    private String moveAndNotify(Field from, Field to, ColorEnum playersColor) {
        boolean won = false;
        getMap().put(from, ColorEnum.WHITE);
        getMap().put(to, playersColor);

        //If checker goes into endPoints area, counter is increased by one for this player
        if(!from.isEndPoint(playersColor) && to.isEndPoint(playersColor)){
            if(atEndPoints.get(playersColor) >= 10) won = true;
            atEndPoints.put(playersColor, atEndPoints.get(playersColor)+1);
        }

        if(won) {
            matchEnded = true;
            map.notifyAll();
            return "WIN";
        }
        movingPlayer = (movingPlayer + 1) % maxPlayers;
        map.notifyAll();
        return "OK";
    }

    private Point getMiddle(Field f1, Field f2) {
        Double x = (f1.x + f2.x + map.getFieldSize()) / 2.0;
        Double y = (f1.y + f2.y + map.getFieldSize()) / 2.0;
        return new Point(x.intValue(), y.intValue());
    }

    private double distance(Field field, Field f) {
        return sqrt(pow(abs(field.x - f.x), 2) +
                        pow(abs(field.y - f.y), 2)
                    );
    }

    public String getGameMaster() {
        return gameMaster;
    }
}

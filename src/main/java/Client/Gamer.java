package Client;

import Map.ColorEnum;
import Map.Field;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;


public class Gamer implements Runnable {
    private final String serverIP;
    private final int port;
    public final ConcurrentHashMap<Field, ColorEnum> map;
    PrintWriter gameWriter;
    private BufferedReader gameReader;
    ColorEnum myColor, currentColor;
    private GameGUI gameGUI;
    Field from = null, to = null;
    private String line;
    private Socket gameSocket;


    Gamer(String serverIP, int port) {
        this.serverIP = serverIP;
        this.port = port;
        map = new ConcurrentHashMap<>();
    }

    @Override
    public void run() {
        try {
            gameSocket = new Socket(serverIP, port);
            gameReader = new BufferedReader(new InputStreamReader(gameSocket.getInputStream()));
            gameWriter = new PrintWriter(gameSocket.getOutputStream(), true);
            getMyColor();


        } catch (IOException e) {
            System.out.println("Could not connect to server");
        }

        gameGUI = new GameGUI(this);

        while (true) {
            try {
                line = gameReader.readLine();
                System.out.println(line);

                if (line.equalsIgnoreCase("SUCCESSFUL")) {
                    System.out.println("Move successful");
                    gameGUI.disableMoving();
                    applyMove();
                }
                else if (line.equalsIgnoreCase("MAP")) {
                    getMap();
                }
                else if (line.equalsIgnoreCase("MOVE")) {
                    getMap();
                    System.out.println("Able to Move");
                    gameGUI.allowMoving();
                    gameGUI.repaint();
                } else if(line.startsWith("WIN")) {
                    System.out.println(line);
                    gameSocket.close();
                    return;
                }

                from = null; to = null;
            } catch (IOException ex) {
                return;
            }
        }
    }

    private void applyMove() {
        synchronized (map) {
            map.put(from, ColorEnum.WHITE);
            map.put(to, myColor);
        }
        gameGUI.repaint();
    }

    private void getMyColor() {
        gameWriter.println("GETCOLOR");
        try {
            String line = gameReader.readLine();
            myColor = ColorEnum.valueOf(line);
            System.out.println("MyColor: " + myColor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getMap() {
        synchronized (map) {
            try {
                map.clear();
                String line;
                while (true) {
                    line = gameReader.readLine();
                    if(line.equalsIgnoreCase("END")) break;
                    String[] parameters = line.split(" ");
                    if(parameters.length == 1) {
                        currentColor = ColorEnum.valueOf(parameters[0]);
                        break;
                    }
                    map.put(new Field(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1])), ColorEnum.valueOf(parameters[2]));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        gameGUI.repaint();
    }

    public synchronized ConcurrentHashMap<Field, ColorEnum> getFieldList() {
        return map;
    }
}

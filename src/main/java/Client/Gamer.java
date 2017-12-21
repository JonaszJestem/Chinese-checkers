package Client;

import Map.ColorEnum;
import Map.Field;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class Gamer implements Runnable {
    private final String serverIP;
    private final int port;
    public Socket gameSocket;
    volatile HashMap<Field, ColorEnum> map = new HashMap<>();
    private PrintWriter gameWriter;
    private BufferedReader gameReader;
    private ColorEnum myColor;
    private GameGUI gameGUI;

    public Gamer(String serverIP, int port) {
        this.serverIP = serverIP;
        this.port = port;
    }

    private void getMyColor() {
        gameWriter.println("GETCOLOR");
        try {
            String line = gameReader.readLine();
            myColor = ColorEnum.valueOf(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            gameSocket = new Socket(serverIP, port);
            gameReader = new BufferedReader(new InputStreamReader(gameSocket.getInputStream()));
            gameWriter = new PrintWriter(gameSocket.getOutputStream(), true);
            //getMyColor();


        } catch (IOException e) {
            e.printStackTrace();
        }
        getMap();
        gameGUI = new GameGUI(this);
        /*
        while(true) {
            try {
                String line = gameReader.readLine();
                if(line.equalsIgnoreCase("YOURTURN")) {
                    //makeMove();
                }
                else if(line.equalsIgnoreCase("MAP")) {

                }
                sleep(100);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }*/
    }

    void getMap() {
        try {
            gameWriter.println("GETMAP");
            map.clear();
            String line;
            while (true) {
                line = gameReader.readLine();
                if (line.equalsIgnoreCase("END")) break;
                String[] parameters = line.split(" ");
                map.put(new Field(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1])), ColorEnum.valueOf(parameters[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendMove(Point p, Point q) {
        Field from = null, to = null;
        for (Field f : map.keySet()) {
            if (f.contains(p)) from = f;
            else if (f.contains(q)) to = f;
        }

        if (from != null && to != null) {
            try {
                gameWriter.println("MOVE " + from.x_int + " " + from.y_int + " " + to.x_int + " " + to.y_int + "\n");
                String line;
                while (true) {
                    line = gameReader.readLine();
                    if (line.equalsIgnoreCase("SUCCESSFUL")) {
                        getMap();
                        gameGUI.repaint();
                        waitForMove();
                        break;
                    } else if (line.equalsIgnoreCase("WRONGMOVE")) {
                        getMap();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitForMove() {
    }
}

package Server.Bot;
import Map.ColorEnum;
import Map.Field;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.Math.abs;
import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;

public class Bot implements Runnable {

    private final String serverIP;
    private final int port;
    public final HashMap<Field, ColorEnum> map;
    PrintWriter gameWriter;
    private BufferedReader gameReader;
    ColorEnum myColor, currentColor;
    Field from = null, to = null;
    private String line;
    private Socket gameSocket;
    private Field[] myFields = null;
    private ArrayList<Pair<Field, Field>> moves;


    public Bot(String serverIP, int port) {
        this.serverIP = serverIP;
        this.port = port;
        map = new HashMap<>();
    }

    @Override
    public void run() {
        try {
            gameSocket = new Socket(serverIP, port);
            gameReader = new BufferedReader(new InputStreamReader(gameSocket.getInputStream()));
            gameWriter = new PrintWriter(gameSocket.getOutputStream(), true);
            getMyColor();
        } catch (IOException e) {
            System.out.println("Bot not connected");
        }

        while (true) {
            try {
                System.out.println("Bot: Waiting for move");
                line = gameReader.readLine();
                System.out.println(line);
                if (line.equalsIgnoreCase("MAP")) {
                    getMap();
                    findMyFields();
                }
                else if (line.equalsIgnoreCase("MOVE")) {
                    getMap();
                    findMyFields();
                    System.out.println("Able to Move");
                    executeMove();
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

    private void findMyFields() {
        myFields = new Field[10];
        int i = 0;
        for(Entry<Field, ColorEnum> entry: map.entrySet()) {
            if(entry.getValue().equals(myColor)) {
                myFields[i] = entry.getKey();
                i++;
            }
        }
    }

    private void executeMove() {
        findAvailableMoves();
        //TODO: Implement choosing best move found, not the random one
        int rand = moves.size() - 1;
        Pair<Field,Field> move = moves.get(rand);

        gameWriter.println("MOVE " + move.fst.x_int + " " + move.fst.y_int + " " + move.snd.x_int + " " + move.snd.y_int + " " + myColor);
    }

    private void findAvailableMoves() {
        moves.clear();
        for(Field f: myFields) {
            for(Entry<Field, ColorEnum> entry: map.entrySet()) {
                if(checkMove(f, entry)) moves.add(new Pair(f, entry.getKey()));
            }
        }
    }

    private boolean checkMove(Field from, Entry<Field, ColorEnum> to) {
        if(to.getValue() != ColorEnum.WHITE) return false;
        double distance = distance(from, to.getKey());
        if(distance > 90 ) return false;
        //TODO: check if From -> To is valid move

        return false;
    }

    private void getMyColor() {
        gameWriter.println("GETCOLOR");
        try {
            String line = gameReader.readLine();
            myColor = ColorEnum.valueOf(line);
            System.out.println("BotColor: " + myColor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getMap() {
        synchronized (map) {
            try {
                map.clear();
                while (true) {
                    String line = gameReader.readLine();
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
    }

    private Point getMiddle(Field f1, Field f2) {
        Double x = (f1.x + f2.x + f1.getSize()) / 2.0;
        Double y = (f1.y + f2.y + f1.getSize()) / 2.0;
        return new Point(x.intValue(), y.intValue());
    }

    private double distance(Field field, Field f) {
        return sqrt(pow(abs(field.x - f.x), 2) +
                pow(abs(field.y - f.y), 2)
        );
    }

    class Pair<F, S> {
        public final F fst;
        public final S snd;

        Pair(F f, S t) {
            this.fst = f;
            this.snd = t;
        }
    }
}

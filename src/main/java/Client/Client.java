package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
    //Connection and communication
    private static final int port = 8000;
    private static final String serverIP = "localhost";
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    public boolean isConnected = false;
    public boolean isInGame = false;

    Socket socket;
    //User stored variables
    private ArrayList<String> games = new ArrayList<>();
    private String userName;


    void setUserName(String userName) {
        this.userName = userName;
    }
    String getUserName() {
        return userName;
    }

    // ----------------------------------------------------------
    // Server communication
    // ----------------------------------------------------------

    public void connect() throws IOException {
        if (isConnected) return;
        socket = new Socket(serverIP, port);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printWriter = new PrintWriter(socket.getOutputStream(), true);
        isConnected = true;
    }

    void disconnect() {
        printWriter.println("QUIT");
        printWriter.flush();
    }

    public void getGamesFromServer() {
        try {
            bufferedReader.ready();
            printWriter.println("GETGAMES");
            String line = bufferedReader.readLine();
            parseGamesFromServer(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseGamesFromServer(String line) {
        games.clear();
        if (line.length() == 0) return;
        String[] gameList = line.split(";");
        for (String g : gameList) {
            games.add(g.split(" ")[0] + " " + g.split(" ")[1]);
        }
    }

    public ArrayList<String> getGames() {
        return games;
    }

    void addGame(String text, int possiblePlayers) {
        if(text.equalsIgnoreCase("")) text = "newGame";
        printWriter.println("CREATEGAME " + text.replaceAll("\\s+", "") + " " + possiblePlayers);
        printWriter.flush();
    }

    public void joinGame(int id) {
        try {
            bufferedReader.ready();
            printWriter.println("JOIN " + id);
            String line = bufferedReader.readLine();
            if (line.startsWith("YES")) {
                Gamer gamer = new Gamer(serverIP, 50000 + id);

                new Thread(gamer).start();
                isInGame = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ----------------------------------------------------------
    // Server.Game communication
    // ----------------------------------------------------------
/*
    public void getColor() {
        try {
            gameWriter.println("GETCOLOR");
            while (true) {
                line = gameReader.readLine();
                System.out.println(line);
                if (line.equalsIgnoreCase("END")) break;
                myColor = ColorEnum.valueOf(line);
                System.out.println(myColor);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getMap() {
        try {
            gameWriter.println("GETMAP");
            HashMap<Field,ColorEnum> fieldsFromServer = new HashMap<>();
            while (true) {
                line = gameReader.readLine();
                System.out.println(line);
                if (line.equalsIgnoreCase("END")) break;
                String[] parameters = line.split(" ");
                fieldsFromServer.put(new Field(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1])), ColorEnum.valueOf(parameters[2]));
            }
            this.map.setFieldList(fieldsFromServer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMove() {
        gameWriter.println("SETMAP");
        StringBuilder stringBuilder = new StringBuilder();
        map.getFieldList().forEach((k,v) -> stringBuilder.append(k.x + " " + k.y + " " + v.getColor() + "\n"));

        gameWriter.write(stringBuilder.toString() + "END\n");
        gameWriter.flush();

        while(true) {
            try {
                line = gameReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line.equalsIgnoreCase("YOURMOVE")) {
                getMap();
                break;
            }
        }
    }

    public void move(Point moveFrom, Point moveTo) {
        Entry<Field, ColorEnum> from = null;
        Entry<Field, ColorEnum> to = null;

        for (Entry<Field, ColorEnum> f : map.getFieldList().entrySet()) {
            if (f.getKey().contains(moveFrom) && f.getValue().getColor() != ColorEnum.WHITE && myColor.getRGBColor().equals(f.getValue().getRGBColor())) {
                from = f;
            }
            if (f.getKey().contains(moveTo) && f.getValue().getColor() == ColorEnum.WHITE) {
                to = f;
            }
        }

        if (from != null && to != null && distance(to.getKey(), from.getKey()) < 90) {
            if (distance(from.getKey(), to.getKey()) <= 45) {
                to.setValue(from.getValue());
                from.setValue(ColorEnum.WHITE);
            } else if (distance(to.getKey(), from.getKey()) <= 90) {
                Point middle = getMiddle(from.getKey(), to.getKey());
                for (Entry<Field, ColorEnum> f : map.getFieldList().entrySet()) {
                    if (f.getKey().contains(middle) && f.getValue().getColor() != ColorEnum.WHITE) {
                        to.setValue(from.getValue());
                        from.setValue(ColorEnum.WHITE);
                        break;
                    }
                }
            }
            sendMove();
        }
    }

    private Point getMiddle(Field f1, Field f2) {
        return new Point(abs(new Double((f1.x + f2.x + map.getFieldSize()) / 2).intValue()),
                abs(new Double((f1.y + f2.y + map.getFieldSize()) / 2).intValue()));
    }

    private double distance(Field field, Field f) {
        System.out.println("Distance: " + sqrt(pow(abs(field.x - f.x), 2) + pow(abs(field.y - f.y), 2)));
        return sqrt(pow(abs(field.x - f.x), 2) + pow(abs(field.y - f.y), 2));
    }*/
}

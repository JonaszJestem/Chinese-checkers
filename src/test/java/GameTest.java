import Server.Server;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import Map.*;

import Client.*;
import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTest {
    private static Server s;
    private static Thread st;
    private static ArrayList<ClientThread> clients = new ArrayList<>();

    @BeforeClass
    public static void setUpServer() {
        s = new Server();
        st = new Thread(s);
        st.start();

        while (!s.isRunning()) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        s.addGame("TestGame", 4);
    }

    @BeforeClass
    public static void setUpClients() {
        for (int i = 0; i < 10; i++) {
            clients.add(new ClientThread());
            clients.get(i).run();
        }
    }

    @Test(timeout = 10000)
    public void gamerShouldGetMapFromServer() {
        Client c1 = clients.get(0).getClient();
        Client c2 = clients.get(1).getClient();
        try {
            c1.connect();
            c2.connect();


            c1.joinGame(0);
            c2.joinGame(0);
        } catch (IOException ex) {
            System.out.println("IOException");
            return;
        }


        while(c1.gamer.getFieldList().isEmpty() || c2.gamer.getFieldList().isEmpty()) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(Field f: c1.gamer.getFieldList().keySet()) {
            assertEquals(c1.gamer.getFieldList().get(f), (s.getGame(0).map.getFieldList().get(f)));
        }

        assertEquals(c1.gamer.getFieldList(), s.getGame(0).map.getFieldList());
        assertEquals(c2.gamer.getFieldList(), s.getGame(0).map.getFieldList());
    }

    //@Test
    public void gamerShouldBeAbleToMove() {
        Client c1 = clients.get(0).getClient();
        Client c2 = clients.get(1).getClient();


        assertEquals(c1.gamer.map, s.getGame(0).map.getFieldList());
        assertEquals(c2.gamer.map, s.getGame(0).map.getFieldList());
    }
}

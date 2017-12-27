import Server.Server;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
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

    @Test
    public void gamerShouldGetMapFromServer() {
        Client c1 = clients.get(0).getClient();
        Client c2 = clients.get(1).getClient();
        try {
            c1.connect();
            c2.connect();
        } catch (IOException ex) {
            System.out.println("IOException");
            return;
        }

        c1.joinGame(0);
        c2.joinGame(0);

        try {
            sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(c1.gamer.map, s.getGame(0).map.getFieldList());
        assertEquals(c2.gamer.map, s.getGame(0).map.getFieldList());
    }

    @Test
    public void gamerShouldBeAbleToMove() {
        Client c1 = clients.get(0).getClient();
        Client c2 = clients.get(1).getClient();


        assertEquals(c1.gamer.map, s.getGame(0).map.getFieldList());
        assertEquals(c2.gamer.map, s.getGame(0).map.getFieldList());
    }
}
